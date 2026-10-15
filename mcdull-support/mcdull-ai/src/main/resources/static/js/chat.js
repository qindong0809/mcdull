/**
 * mcdull-ai 前端对话页面
 *
 * 对接后端接口：
 *   GET  /ai/roles                 角色预设
 *   GET  /ai/chat/stream           SSE 流式对话（带会话记忆）
 *   GET  /ai/chat                  阻塞对话（备用）
 *   GET  /ai/history/{sessionId}   会话历史
 *   DELETE /ai/history/{sessionId} 清空会话
 */
(function () {
    'use strict';

    var STORAGE_KEY = 'mcdull_ai_sessions_v1';
    var CURRENT_KEY = 'mcdull_ai_current_v1';

    var elMessages = document.getElementById('messages');
    var elInput = document.getElementById('input');
    var elSend = document.getElementById('sendBtn');
    var elStop = document.getElementById('stopBtn');
    var elSessionList = document.getElementById('sessionList');
    var elRoleSelect = document.getElementById('roleSelect');
    var elClear = document.getElementById('clearBtn');

    var sessions = [];          // [{id, title}]
    var currentId = null;
    var controller = null;      // AbortController，用于停止生成
    var generating = false;

    // ====================== 初始化 ======================

    init();

    function init() {
        sessions = loadSessions();
        if (!sessions.length) {
            sessions.push(createSession());
        }
        currentId = localStorage.getItem(CURRENT_KEY);
        if (!findSession(currentId)) {
            currentId = sessions[0].id;
        }
        renderSessionList();
        loadRoles();
        loadHistory();
        bindEvents();
    }

    function bindEvents() {
        elSend.addEventListener('click', sendMessage);
        elStop.addEventListener('click', stopGenerate);
        elClear.addEventListener('click', clearCurrentSession);
        document.getElementById('newChatBtn').addEventListener('click', newSession);

        elInput.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        });

        // 输入框自适应高度
        elInput.addEventListener('input', function () {
            elInput.style.height = 'auto';
            elInput.style.height = Math.min(elInput.scrollHeight, 160) + 'px';
        });
    }

    // ====================== 会话管理 ======================

    function createSession() {
        return { id: 's-' + Date.now().toString(36) + Math.random().toString(36).slice(2, 6), title: '新对话' };
    }

    function findSession(id) {
        return sessions.find(function (s) { return s.id === id; });
    }

    function loadSessions() {
        try {
            var arr = JSON.parse(localStorage.getItem(STORAGE_KEY));
            return Array.isArray(arr) ? arr : [];
        } catch (e) {
            return [];
        }
    }

    function saveSessions() {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(sessions));
        localStorage.setItem(CURRENT_KEY, currentId);
    }

    function renderSessionList() {
        elSessionList.innerHTML = '';
        sessions.forEach(function (s) {
            var item = document.createElement('div');
            item.className = 'session-item' + (s.id === currentId ? ' active' : '');

            var title = document.createElement('span');
            title.className = 'title';
            title.textContent = s.title;
            title.addEventListener('click', function () { switchSession(s.id); });

            var del = document.createElement('button');
            del.className = 'del';
            del.textContent = '✕';
            del.title = '删除会话';
            del.addEventListener('click', function (e) { e.stopPropagation(); deleteSession(s.id); });

            item.appendChild(title);
            item.appendChild(del);
            elSessionList.appendChild(item);
        });
        saveSessions();
    }

    function switchSession(id) {
        if (id === currentId) return;
        stopGenerate();
        currentId = id;
        renderSessionList();
        loadHistory();
    }

    function newSession() {
        var s = createSession();
        sessions.unshift(s);
        currentId = s.id;
        renderSessionList();
        loadHistory();
        elInput.focus();
    }

    function deleteSession(id) {
        sessions = sessions.filter(function (s) { return s.id !== id; });
        fetch('/ai/history/' + id, { method: 'DELETE' }).catch(function () { });
        if (!sessions.length) {
            sessions.push(createSession());
        }
        if (currentId === id) {
            currentId = sessions[0].id;
            loadHistory();
        }
        renderSessionList();
    }

    function clearCurrentSession() {
        if (!confirm('确定清空当前会话的上下文记忆吗？')) return;
        stopGenerate();
        fetch('/ai/history/' + currentId, { method: 'DELETE' }).catch(function () { });
        loadHistory();
    }

    // ====================== 角色 / 历史 ======================

    function loadRoles() {
        fetch('/ai/roles')
            .then(function (r) { return r.json(); })
            .then(function (roles) {
                elRoleSelect.innerHTML = '';
                roles.forEach(function (r) {
                    var opt = document.createElement('option');
                    opt.value = r.id;
                    opt.textContent = r.name + '（' + r.description + '）';
                    elRoleSelect.appendChild(opt);
                });
            })
            .catch(function () { /* 角色加载失败不影响使用 */ });
    }

    function loadHistory() {
        hideWelcome();
        fetch('/ai/history/' + encodeURIComponent(currentId))
            .then(function (r) { return r.json(); })
            .then(function (list) {
                elMessages.innerHTML = '';
                if (!list || !list.length) {
                    showWelcome();
                    return;
                }
                list.forEach(function (m) { appendMessage(m.role, m.content); });
                scrollToBottom(true);
            })
            .catch(showWelcome);
    }

    function showWelcome() {
        if (!elMessages.querySelector('.welcome')) {
            elMessages.innerHTML =
                '<div class="welcome"><h2>mcdull-ai 学习助手</h2>' +
                '<p>基于 Spring AI 1.1.x + 智谱 glm-4-plus，支持会话记忆、SSE 流式输出、角色预设与思考过程回显。</p>' +
                '<p>试试问我：「用一句话介绍 Spring AI 的 ChatClient」</p></div>';
        }
    }

    function hideWelcome() {
        var w = elMessages.querySelector('.welcome');
        if (w) w.remove();
    }

    // ====================== 发送 / 流式渲染 ======================

    function sendMessage() {
        var text = elInput.value.trim();
        if (!text || generating) return;

        elInput.value = '';
        elInput.style.height = 'auto';
        hideWelcome();

        // 会话标题取首条消息
        var session = findSession(currentId);
        if (session && session.title === '新对话') {
            session.title = text.length > 16 ? text.slice(0, 16) + '…' : text;
            renderSessionList();
        }

        appendMessage('user', text);
        var bubble = appendMessage('assistant', '');
        setGenerating(true);

        streamChat(text, bubble);
    }

    function streamChat(text, bubble) {
        var role = elRoleSelect.value;
        var enableThinking = document.getElementById('thinkingToggle').checked;
        var url = '/ai/chat/stream?sessionId=' + encodeURIComponent(currentId) +
            '&message=' + encodeURIComponent(text) +
            '&role=' + encodeURIComponent(role) +
            '&enableThinking=' + enableThinking;

        controller = new AbortController();
        var anyContent = false;

        fetch(url, { signal: controller.signal })
            .then(function (res) {
                if (!res.ok) {
                    return res.text().then(function (t) {
                        throw new Error('HTTP ' + res.status + (t ? '：' + t.slice(0, 200) : ''));
                    });
                }
                var reader = res.body.getReader();
                var decoder = new TextDecoder('utf-8');
                var buf = '';

                function pump() {
                    return reader.read().then(function (result) {
                        if (result.done) { finish(); return; }
                        buf += decoder.decode(result.value, { stream: true });
                        // SSE 事件以空行分隔
                        var events = buf.split('\n\n');
                        buf = events.pop();
                        events.forEach(function (ev) {
                            var evtName = 'message';
                            var dataLines = [];
                            ev.split('\n').forEach(function (line) {
                                if (line.indexOf('event:') === 0) {
                                    evtName = line.slice(6).trim();
                                } else if (line.indexOf('data:') === 0) {
                                    var d = line.slice(5);
                                    if (d.charAt(0) === ' ') d = d.slice(1);
                                    if (d === '[DONE]') return;
                                    dataLines.push(d);
                                }
                            });
                            var data = dataLines.join('\n');
                            if (!data) return;
                            handleSseEvent(bubble, evtName, data);
                            anyContent = true;
                        });
                        scrollToBottom();
                        return pump();
                    });
                }
                return pump();
            })
            .catch(function (err) {
                if (err.name === 'AbortError') { finish(); return; }
                bubble.classList.add('error');
                bubble._contentText = '请求失败：' + err.message;
                updateBubble(bubble, false);
                finish();
            });

        function finish() {
            if (!anyContent) {
                bubble._contentText = '（模型未返回内容）';
            }
            updateBubble(bubble, false);
            setGenerating(false);
            controller = null;
            scrollToBottom();
        }
    }

    /**
     * 处理单个 SSE 事件：
     *   event=thinking -> 原生 reasoning，追加到思考过程
     *   event=content  -> 正文，可能包含 <thinking>...</thinking>，需解析拆分
     */
    function handleSseEvent(bubble, evtName, data) {
        if (evtName === 'thinking') {
            bubble._thinkingText += data;
            updateBubble(bubble, true);
            return;
        }
        // content 事件：解析 <thinking> 标签（可能跨多个 chunk）
        bubble._rawBuf += data;
        var buf = bubble._rawBuf;
        while (true) {
            if (!bubble._inThinking) {
                var sIdx = buf.indexOf('<thinking>');
                if (sIdx >= 0) {
                    if (sIdx > 0) bubble._contentText += buf.slice(0, sIdx);
                    buf = buf.slice(sIdx + '<thinking>'.length);
                    bubble._inThinking = true;
                } else {
                    // 防止把不完整的 <thinking 当作正文
                    var lt = buf.lastIndexOf('<');
                    if (lt >= 0 && buf.length - lt < 10 && buf.indexOf('>', lt) < 0) {
                        if (lt > 0) bubble._contentText += buf.slice(0, lt);
                        buf = buf.slice(lt);
                    } else {
                        bubble._contentText += buf;
                        buf = '';
                    }
                    break;
                }
            } else {
                var eIdx = buf.indexOf('</thinking>');
                if (eIdx >= 0) {
                    bubble._thinkingText += buf.slice(0, eIdx);
                    buf = buf.slice(eIdx + '</thinking>'.length);
                    bubble._inThinking = false;
                } else {
                    var lt2 = buf.lastIndexOf('<');
                    if (lt2 >= 0 && buf.length - lt2 < 12 && buf.indexOf('>', lt2) < 0) {
                        if (lt2 > 0) bubble._thinkingText += buf.slice(0, lt2);
                        buf = buf.slice(lt2);
                    } else {
                        bubble._thinkingText += buf;
                        buf = '';
                    }
                    break;
                }
            }
        }
        bubble._rawBuf = buf;
        updateBubble(bubble, true);
    }

    function stopGenerate() {
        if (controller) controller.abort();
    }

    function setGenerating(on) {
        generating = on;
        elSend.disabled = on;
        elStop.hidden = !on;
    }

    /**
     * 追加一条消息气泡，返回气泡 DOM。
     * assistant 气泡内部结构：.thinking-block（可折叠思考过程） + .bubble-content（正文）
     */
    function appendMessage(role, content) {
        var wrap = document.createElement('div');
        wrap.className = 'msg ' + role;

        var avatar = document.createElement('div');
        avatar.className = 'avatar';
        avatar.textContent = role === 'user' ? '🧑' : '🤖';

        var bubble = document.createElement('div');
        bubble.className = 'bubble';

        if (role === 'assistant') {
            // 思考过程折叠块
            var tpl = document.createElement('template');
            tpl.innerHTML =
                '<div class="thinking-block">' +
                '<div class="thinking-head"><span><span class="dot"></span>思考过程 <span class="thinking-state"></span></span><span class="arrow">▶</span></div>' +
                '<div class="thinking-body empty">模型未输出思考过程</div>' +
                '</div>' +
                '<div class="bubble-content"></div>';
            while (tpl.content.firstChild) bubble.appendChild(tpl.content.firstChild);

            bubble._thinkingEl = bubble.querySelector('.thinking-block');
            bubble._thinkingBody = bubble.querySelector('.thinking-body');
            bubble._thinkingState = bubble.querySelector('.thinking-state');
            bubble._contentEl = bubble.querySelector('.bubble-content');
            bubble._thinkingText = '';
            bubble._contentText = '';
            bubble._inThinking = false;
            bubble._rawBuf = '';

            bubble._thinkingEl.querySelector('.thinking-head').addEventListener('click', function () {
                bubble._thinkingEl.classList.toggle('open');
            });
        }

        wrap.appendChild(avatar);
        wrap.appendChild(bubble);
        elMessages.appendChild(wrap);

        if (content) {
            if (role === 'assistant') {
                // 历史消息不解析 <thinking>，直接当正文（历史接口未返回思考过程）
                bubble._contentText = content;
                updateBubble(bubble, false);
            } else {
                bubble.textContent = content;
            }
        }
        return bubble;
    }

    /**
     * 更新 assistant 气泡：思考过程区 + 正文区。streaming 时正文末尾加打字光标。
     */
    function updateBubble(bubble, streaming) {
        // 思考过程
        if (bubble._thinkingText) {
            bubble._thinkingBody.textContent = bubble._thinkingText;
            bubble._thinkingBody.classList.remove('empty');
            bubble._thinkingEl.style.display = '';
            bubble._thinkingState.textContent = streaming ? '（思考中…）' : '';
        } else {
            bubble._thinkingEl.style.display = streaming ? '' : 'none';
            bubble._thinkingBody.textContent = '模型未输出思考过程';
            bubble._thinkingBody.classList.add('empty');
            bubble._thinkingState.textContent = '';
        }
        // 正文
        bubble._contentEl.innerHTML = markdownLite(bubble._contentText) +
            (streaming ? '<span class="cursor"></span>' : '');
        bindCopyButtons(bubble._contentEl);
    }

    function scrollToBottom(force) {
        var nearBottom = elMessages.scrollHeight - elMessages.scrollTop - elMessages.clientHeight < 120;
        if (force || nearBottom) {
            elMessages.scrollTop = elMessages.scrollHeight;
        }
    }

    // ====================== 轻量 Markdown 渲染 ======================

    function escapeHtml(s) {
        return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;').replace(/'/g, '&#39;');
    }

    /**
     * 轻量 Markdown：标题 / 加粗 / 斜体 / 行内代码 / 代码块 / 列表 / 段落。
     * 零依赖实现，先转义 HTML 再转换，避免 XSS。
     */
    function markdownLite(src) {
        if (!src) return '';
        var codes = [];
        // 1. 抽取代码块为占位符，避免内部内容被二次转换
        var text = escapeHtml(src).replace(/```(\w*)\n?([\s\S]*?)(```|$)/g, function (m, lang, code) {
            var idx = codes.length;
            codes.push({ lang: lang || 'text', code: code.replace(/\n$/, '') });
            return '\u0000CODE' + idx + '\u0000';
        });

        // 2. 行内元素
        text = text
            .replace(/`([^`\n]+)`/g, '<code>$1</code>')
            .replace(/\*\*([^*\n]+)\*\*/g, '<strong>$1</strong>')
            .replace(/\*([^*\n]+)\*/g, '<em>$1</em>');

        // 3. 逐行处理：标题 / 列表 / 段落
        var lines = text.split('\n');
        var html = [];
        var inList = false;

        lines.forEach(function (line) {
            var trimmed = line.trim();
            var codeMatch = trimmed.match(/^\u0000CODE(\d+)\u0000$/);

            if (codeMatch) {
                closeList();
                var c = codes[+codeMatch[1]];
                html.push(
                    '<div class="code-block"><div class="code-lang"><span>' + escapeHtml(c.lang) + '</span>' +
                    '<button class="copy-btn">复制</button></div>' +
                    '<pre><code>' + c.code + '</code></pre></div>'
                );
                return;
            }
            if (!trimmed) { closeList(); return; }

            var h = trimmed.match(/^(#{1,4})\s+(.*)$/);
            if (h) {
                closeList();
                html.push('<h' + h[1].length + '>' + h[2] + '</h' + h[1].length + '>');
                return;
            }
            if (/^[-*]\s+/.test(trimmed)) {
                if (!inList) { html.push('<ul>'); inList = true; }
                html.push('<li>' + trimmed.replace(/^[-*]\s+/, '') + '</li>');
                return;
            }
            if (/^\d+\.\s+/.test(trimmed)) {
                if (!inList) { html.push('<ul>'); inList = true; }
                html.push('<li>' + trimmed.replace(/^\d+\.\s+/, '') + '</li>');
                return;
            }
            closeList();
            html.push('<p>' + trimmed + '</p>');
        });
        closeList();

        function closeList() {
            if (inList) { html.push('</ul>'); inList = false; }
        }

        return html.join('');
    }

    function bindCopyButtons(scope) {
        scope.querySelectorAll('.copy-btn').forEach(function (btn) {
            btn.onclick = function () {
                var code = btn.closest('.code-block').querySelector('pre code');
                navigator.clipboard.writeText(code.textContent).then(function () {
                    btn.textContent = '已复制';
                    setTimeout(function () { btn.textContent = '复制'; }, 1500);
                });
            };
        });
    }
})();
