package io.gitee.dqcer.mcdull.devtools.notify.git;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 把 git remote URL 归一化为可点击 web URL，并按 host 生成 commit 深链。
 * <p>
 * 覆盖 GitHub / Gitee / GitLab / Coding，其他 host 走通用模板。
 *
 * @author dqcer
 */
public final class RepoUrlParser {

    private static final Pattern SCP_LIKE = Pattern.compile("^[^@/]+@([^:]+):(.+?)(?:\\.git)?$");

    private static final Pattern HTTPS = Pattern.compile("^(?:https?)://(?:[^@/]+@)?([^/]+)/(.+?)(?:\\.git)?/?$");

    private RepoUrlParser() {
    }

    /**
     * 任意 remote URL → https web URL；解析失败返回 null。
     */
    public static String toWebUrl(String remote) {
        if (remote == null || remote.isBlank()) {
            return null;
        }
        Matcher scp = SCP_LIKE.matcher(remote);
        if (scp.matches()) {
            return "https://" + scp.group(1) + "/" + scp.group(2);
        }
        Matcher http = HTTPS.matcher(remote);
        if (http.matches()) {
            return "https://" + http.group(1) + "/" + http.group(2);
        }
        return null;
    }

    /**
     * 按 host 选 commit 链接模板。
     */
    public static String commitLink(String webUrl, String shortSha) {
        if (webUrl == null || shortSha == null) {
            return null;
        }
        String host = extractHost(webUrl);
        if (host == null) {
            return webUrl + "/commit/" + shortSha;
        }
        if (host.contains("gitlab") || host.contains("coding")) {
            return webUrl + "/-/commit/" + shortSha;
        }
        return webUrl + "/commit/" + shortSha;
    }

    private static String extractHost(String url) {
        try {
            return java.net.URI.create(url).getHost();
        } catch (Exception e) {
            return null;
        }
    }
}
