
> Facade 模块 `mvn deploy` 到私服后，自动向 IM 群（钉钉 / 企业微信 / 飞书）推送一条
> **"发布了什么 · 改了什么 · 影响谁"** 的卡片。纯 Java、业务无感、永不阻断构建。

---

## 一、解决什么问题

微服务团队协作里有一个每天都在发生、却一直没有被自动化的小痛点：

> 上游把 `xxx-facade` 改了、`mvn deploy` 发到私服，**下游根本不知道**，
> 要么靠人在群里吼一句"我 facade 更新了你们重拉一下"，要么等下游 CI 编译失败才发现。

本插件把这件事变成 **deploy 的自动副作用**：

| 没有它 | 有了它 |
|---|---|
| 人肉在群里 @ | deploy 结束自动推卡片 |
| "更新了" 三个字，下游还得自己 diff | 自带最近 3 条 commit 摘要 |
| 不知道谁受影响 | 自动列出 reactor 内的下游模块 |
| 容易忘、容易漏 | 绑在 deploy 阶段，忘不掉 |

**设计三原则**（贯穿全部实现）：
1. **业务无感** —— 业务同学 pom 只写 3 行引用，版本/配置由父 pom 收敛
2. **永不阻断构建** —— 通知失败只 warn，绝不让 `mvn deploy` 变红
3. **信息有价值** —— 只发"发布完了"是噪声；必须带**变更摘要 + 下游影响**

---

## 二、工作原理

```
mvn deploy (某个 *-facade 模块)
        │
        ▼  绑定 deploy 阶段自动触发 goal: notify
   NotifyMojo.execute()
        │  ① enabled=false?  SNAPSHOT 跳过?  webhook 空?  → 静默退出
        ▼
   GitContextResolver ── JGit 发现 .git → 失败则读 git.properties → 再失败则降级
        │
   ReactorDownstreamAnalyzer ── 遍历 session 所有模块，找出直接依赖本 facade 的下游
        │
   MarkdownMessageRenderer ── 组装卡片 + 钉钉 markdown JSON
        │
   WebhookSender.sendJson ── JDK HttpClient POST，2s 连接 / 3s 请求超时，异常吞掉
```

---

## 三、快速开始（3 步）

### 第 1 步：给需要通知的 facade 模块加 3 行

```xml
<!-- xxx-facade/pom.xml，根 pom 的 pluginManagement 已收敛版本与配置 -->
<build>
    <plugins>
        <plugin>
            <groupId>io.gitee.dqcer</groupId>
            <artifactId>mcdull-devtools-notify</artifactId>
        </plugin>
    </plugins>
</build>
```

> 本仓库已默认给 `system / blaze / focus / workflow / demo` 五个 facade 加好。

### 第 2 步：配置一个群机器人 webhook（见第四节）

```powershell
# 最直接：命令行临时指定
mvn deploy "-Dmcdull.notify.webhook=https://oapi.dingtalk.com/robot/send?access_token=XXXX"
```

### 第 3 步：正常 deploy，观察群里收到卡片

就这么简单。不配 webhook 时插件**静默不发**（默认关闭，安全）。

---

## 四、配置详解

### 4.1 三个参数

| 参数（`-D` 属性名） | 默认值 | 作用 |
|---|---|---|
| `mcdull.notify.enabled` | `true` | 总开关。`false` 时立即静默退出 |
| `mcdull.notify.skipSnapshot` | `true` | `true` 时跳过 `-SNAPSHOT` 版本，避免开发期刷屏 |
| `mcdull.notify.webhook` | **空** | 目标群机器人 URL。**空则不发**（默认安全态） |

### 4.2 覆盖优先级（从高到低）

```
1  命令行        mvn deploy -Dmcdull.notify.webhook=...
2  环境变量      MAVEN_OPTS=-Dmcdull.notify.webhook=...
3  settings.xml  profile（推荐，见下）
4  模块 pom      <configuration><webhook>...</webhook></configuration>
5  根 pom        <properties>（当前默认为空 = 关闭）
```

### 4.3 推荐：在 `~/.m2/settings.xml` 里配一次，永久生效且不入库

```xml
<profile>
  <id>mcdull-notify</id>
  <properties>
    <mcdull.notify.webhook>https://oapi.dingtalk.com/robot/send?access_token=XXXX</mcdull.notify.webhook>
    <mcdull.notify.skipSnapshot>true</mcdull.notify.skipSnapshot>
  </properties>
  <activation><activeByDefault>true</activeByDefault></activation>
</profile>
```

### 4.4 各家 IM 的 webhook 获取

| 平台 | 群机器人 webhook 形态 | 备注 |
|---|---|---|
| 钉钉 | `https://oapi.dingtalk.com/robot/send?access_token=...` | 当前消息格式为钉钉 markdown，开箱即用 |
| 企业微信 | `https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=...` | msgtype 同为 `markdown`，多数场景直接可用 |
| 飞书 | `https://open.feishu.cn/open-apis/bot/v2/hook/...` | 需 `interactive` 卡片格式，需扩展渲染层（见第八节） |

> 当前版本对**钉钉系 markdown** 最贴合；企微/飞书如需精细排版，见第八节扩展点。

---

## 五、消息长什么样

```
📦 McDull 发布 · blaze-facade 3.6.0-rc1

版本 3.6.0-rc1 · 分支 architecture-facade-isolation · 时间 15:23

最近变更
- feat(user): 新增部门树查询接口  [a1b2c3d]  · dqcer
- refactor(enum): IEnum 增加 i18n key  [e4f5g6h]  · sanji
- fix(cache): CacheExpire floatRange 修  [i7j8k9l]  · duck

下游影响
- focus-impl
- workflow-impl
```

commit 短链会按 host 自动生成可点击地址：

| remote 类型 | 生成的 commit 链接 |
|---|---|
| `https://gitee.com/dqcer/mcdull.git` | `https://gitee.com/dqcer/mcdull/commit/a1b2c3d` |
| `git@github.com:user/repo.git` | `https://github.com/user/repo/commit/a1b2c3d` |
| GitLab / Coding | `.../repo/-/commit/a1b2c3d` |

---

## 六、如何验证（不骚扰别人的前提下）

### 6.0 首次构建（新克隆 / CI 从零）

一条命令即可，**无需 bootstrap**：

```powershell
mvn -B -DskipTests clean install
```

之所以能一条搞定，依赖三处工程约定，改动它们会破坏这条命令：

| 约定 | 位置 | 若破坏会怎样 |
|---|---|---|
| 根 pom `<modules>` 顺序：`mcdull-devtools` 在 `mcdull-business` **之前** | 根 `pom.xml` | facade 找不到 notify 插件，报 `Cannot resolve plugin io.gitee.dqcer:mcdull-devtools-notify` |
| `prod` profile 的 `<modules>` **也包含 devtools** | 根 `pom.xml` profiles | `mvn -Pprod` 打包时同样报插件缺失 |
| `maven-plugin-plugin` 版本走**根 pom `pluginManagement`**（不是 BOM properties） | 根 `pom.xml` pluginManagement | 报 `'build.plugins.plugin.version' ... must be a valid version`；因 Maven BOM 通过 `<scope>import</scope>` 只传 `dependencyManagement`、**不传 properties**，属性引用会解析不到 |


### 6.1 dry-run：只构建不打扰

```powershell
# 只 package，不进入 deploy 阶段 → 插件不会执行，验证编译无误
mvn -B -pl mcdull-business/mcdull-business-blaze/blaze-facade -am clean package
```

### 6.2 用自建测试群单独验一次

```powershell
# 先 install 让插件进本地仓库
mvn -B -DskipTests clean install
# 临时指向一个测试群 webhook
mvn -B -pl mcdull-business/mcdull-business-blaze/blaze-facade deploy "-Dmcdull.notify.webhook=https://oapi.dingtalk.com/robot/send?access_token=测试token" "-Dmcdull.notify.skipSnapshot=false"
```

看到 Maven 日志：

```
[INFO] [mcdull-notify] 已推送 blaze-facade
```

即代表链路打通。

### 6.3 手动调用 goal（调试用）

goalPrefix 为 `mcdull-notify`，goal 为 `notify`：

```powershell
mvn mcdull-notify:notify "-Dmcdull.notify.webhook=..."
```

---

## 七、Git 上下文与降级行为

`GitContextResolver` 三层降级，**任一失败都不报错、只降级**：

| 层 | 条件 | 得到的信息 |
|---|---|---|
| ① JGit 自动发现 | 项目有 `.git`（含 worktree/submodule） | 分支 + origin 远程 + 最近 3 条 commit |
| ② `git.properties` | 无 `.git`，但 `target/classes/git.properties` 存在（CI 浅克隆/tar 构建） | 分支 + 1 条 HEAD commit |
| ③ 完全降级 | 都没有 | 消息照发，"最近变更"显示占位 |

**想让层②带上远程 URL**，需给 `git-commit-id-maven-plugin` 额外开启字段：

```xml
<includeOnlyProperties>
  <includeOnlyProperty>git.branch</includeOnlyProperty>
  <includeOnlyProperty>git.commit.id.abbrev</includeOnlyProperty>
  <includeOnlyProperty>git.commit.user.name</includeOnlyProperty>
  <includeOnlyProperty>git.commit.message.short</includeOnlyProperty>
  <includeOnlyProperty>git.remote.origin.url</includeOnlyProperty>
</includeOnlyProperties>
```

---

## 八、扩展点

- **下游发现**：`DownstreamAnalyzer` 已抽为接口，当前唯一实现是 reactor 内反查。未来跨仓库/跨企业场景可新增实现（如 `NexusDownstreamAnalyzer` 通过私服 API 反查 dependents），**Mojo 侧零改动**。
- **消息排版**：若要适配飞书/企微富文本，扩展 `MarkdownMessageRenderer.toJsonPayload`，按 webhook host 分流即可。

---

## 九、常见问题 FAQ

**Q：没配 webhook 会不会报错？**
不会。`mcdull.notify.webhook` 为空时插件走"静默退出"，`BUILD SUCCESS` 照常。默认关闭，安全。

**Q：会不会因为我网不好就卡住构建？**
不会。连接 2s、请求 3s 超时，任何异常都被吞掉转 warn，最多"这条通知没发出去"。

**Q：为什么我本地 deploy 没收到通知？**
大概率 `skipSnapshot=true` 且版本是 `-SNAPSHOT`。加 `-Dmcdull.notify.skipSnapshot=false` 验证，或去掉版本号 SNAPSHOT 后缀。

**Q：CI 里会不会重复刷屏？**
一次 reactor 里多个 facade 会各发一条。降噪：① 保持 `skipSnapshot=true`（CI 快照构建天然不发）② webhook 端做 5 分钟合并去重。

**Q：通知会不会早于 artifact 真正上传完成？**
插件绑在 `deploy` 阶段，与 `maven-deploy-plugin` 同阶段，正常情况下在其之后执行。若流水线对时序极敏感，可把通知改绑到显式后置的独立 job。

**Q：能不能只对某几个 facade 生效？**
能。只在想要的 facade pom 里加那 3 行 `<plugin>` 即可，其它模块不引用就不通知。

---

## 十、如何关闭

临时关：

```powershell
mvn deploy "-Dmcdull.notify.enabled=false"
```

彻底移除某模块通知：删掉该 facade pom 里那 3 行 `<plugin>` 引用。

---

## 十一、依赖与版本

- 插件自身依赖在 `mcdull-framework-dependencies` BOM 中统一管理：
  - `org.eclipse.jgit`（Git 发现）
  - `maven-plugin-api` / `maven-core` / `maven-plugin-annotations`（provided）
- 发送能力复用 `mcdull-framework-base` 的 `WebhookSender`（与运行期告警共享一套发送实现）
- 最低 Maven 3.6+ / JDK 17

---
