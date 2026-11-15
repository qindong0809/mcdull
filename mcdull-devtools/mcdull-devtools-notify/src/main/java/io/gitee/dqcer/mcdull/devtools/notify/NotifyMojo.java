package io.gitee.dqcer.mcdull.devtools.notify;

import io.gitee.dqcer.mcdull.devtools.notify.git.GitContextResolver;
import io.gitee.dqcer.mcdull.devtools.notify.impact.ReactorDownstreamAnalyzer;
import io.gitee.dqcer.mcdull.devtools.notify.model.GitContext;
import io.gitee.dqcer.mcdull.devtools.notify.render.MarkdownMessageRenderer;
import io.gitee.dqcer.mcdull.framework.base.support.WebhookSender;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * Facade 模块 deploy 完成后向 IM webhook 推送变更摘要。
 * <p>
 * 硬性约束：<b>本 Mojo 任何失败都不阻断 Maven 构建</b>。所有异常都在 execute 内部
 * catch 掉、写 warn 日志。通知是研发工具，绝不能反噬构建流水线。
 * <p>
 * 业务同学 pom 里的引用只需要 groupId + artifactId，version 与 configuration 由根 pom
 * 的 pluginManagement 收敛。
 *
 * @author dqcer
 */
@Mojo(name = "notify", defaultPhase = LifecyclePhase.DEPLOY, threadSafe = true)
public class NotifyMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    /**
     * 总开关。false 时静默退出。
     */
    @Parameter(property = "mcdull.notify.enabled", defaultValue = "true")
    private boolean enabled;

    /**
     * SNAPSHOT 版本默认不通知，避免开发期刷屏。
     */
    @Parameter(property = "mcdull.notify.skipSnapshot", defaultValue = "true")
    private boolean skipSnapshot;

    /**
     * 目标 webhook URL，一般通过 settings.xml 或 -D 注入。
     */
    @Parameter(property = "mcdull.notify.webhook")
    private String webhook;

    @Override
    public void execute() throws MojoExecutionException {
        // 早退，四种合法"不发"路径，全部静默返回
        if (!enabled) {
            return;
        }
        if (skipSnapshot && project.getVersion() != null && project.getVersion().endsWith("-SNAPSHOT")) {
            getLog().info("[mcdull-notify] SNAPSHOT 版本，按配置跳过通知");
            return;
        }
        if (webhook == null || webhook.isBlank()) {
            getLog().info("[mcdull-notify] webhook 未配置，跳过通知");
            return;
        }

        // 任何异常都不外抛，构建继续
        try {
            GitContext git = new GitContextResolver(getLog()).resolve(session);
            List<MavenProject> downstream = new ReactorDownstreamAnalyzer(session)
                .findDownstream(project);
            MarkdownMessageRenderer.Rendered rendered =
                new MarkdownMessageRenderer().render(project, git, downstream);

            boolean ok = WebhookSender.sendJson(webhook, rendered.jsonPayload());
            getLog().info("[mcdull-notify] " + (ok ? "已推送 " : "推送失败 ") + project.getArtifactId());
        } catch (Throwable t) {
            getLog().warn("[mcdull-notify] 通知异常，已忽略: " + t);
        }
    }
}
