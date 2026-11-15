package io.gitee.dqcer.mcdull.devtools.notify.render;

import io.gitee.dqcer.mcdull.devtools.notify.git.RepoUrlParser;
import io.gitee.dqcer.mcdull.devtools.notify.model.GitContext;
import org.apache.maven.project.MavenProject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 组装通知消息。输出两段：
 * <ul>
 *   <li>Markdown 正文：用于渲染文本</li>
 *   <li>JSON payload：钉钉 markdown 消息格式（企微 / 飞书差异在 channel 层处理，此处只做钉钉系）</li>
 * </ul>
 *
 * @author dqcer
 */
public class MarkdownMessageRenderer {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public Rendered render(MavenProject target,
                           GitContext git,
                           List<MavenProject> downstream) {
        String title = "McDull 发布 · " + target.getArtifactId() + " " + target.getVersion();
        StringBuilder sb = new StringBuilder(512);
        sb.append("### 📦 ").append(title).append("\n\n");
        sb.append("**版本** ").append(target.getVersion())
            .append(" · **分支** ").append(nullSafe(git.getBranch(), "-"))
            .append(" · **时间** ").append(LocalDateTime.now().format(TIME_FMT))
            .append("\n\n");

        sb.append("**最近变更**\n\n");
        if (git.getRecentCommits().isEmpty()) {
            sb.append("- （无 git 信息或浅克隆）\n");
        } else {
            String webUrl = RepoUrlParser.toWebUrl(git.getRemoteUrl());
            for (GitContext.Commit c : git.getRecentCommits()) {
                String link = RepoUrlParser.commitLink(webUrl, c.shortSha());
                sb.append("- ").append(nullSafe(c.subject(), "(no message)"))
                    .append(link == null ? "" : "  [").append(c.shortSha()).append("](").append(link).append("]")
                    .append(c.authorName() == null ? "" : "  ·  " + c.authorName())
                    .append("\n");
            }
        }

        sb.append("\n**下游影响**\n\n");
        if (downstream.isEmpty()) {
            sb.append("- 本 reactor 内暂无模块直接引用\n");
        } else {
            for (MavenProject p : downstream) {
                sb.append("- ").append(p.getArtifactId()).append("\n");
            }
        }

        String markdown = sb.toString();
        String json = toJsonPayload(title, markdown);
        return new Rendered(title, markdown, json);
    }

    /**
     * 钉钉 markdown 消息体，字段严格按官方 webhook schema。
     */
    private String toJsonPayload(String title, String markdown) {
        String escTitle = escape(title);
        String escMd = escape(markdown);
        return "{\"msgtype\":\"markdown\",\"markdown\":{\"title\":\"" + escTitle
            + "\",\"text\":\"" + escMd + "\"}}";
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "")
            .replace("\t", "  ");
    }

    private static String nullSafe(String s, String fallback) {
        return s == null || s.isBlank() ? fallback : s;
    }

    /**
     * 渲染结果三元组。
     */
    public record Rendered(String title, String markdown, String jsonPayload) {
    }
}
