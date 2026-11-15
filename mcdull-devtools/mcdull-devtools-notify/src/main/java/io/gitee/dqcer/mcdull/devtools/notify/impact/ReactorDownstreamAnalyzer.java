package io.gitee.dqcer.mcdull.devtools.notify.impact;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 同 reactor 内反查下游：遍历 {@link MavenSession#getAllProjects()}，找 pom 中直接依赖 target 的模块。
 * <p>
 * 过滤规则：test scope 与 optional 依赖不算下游。
 * 跨仓库场景不适用（返回空列表），后续由 NexusDownstreamAnalyzer 等实现补充。
 *
 * @author dqcer
 */
public class ReactorDownstreamAnalyzer implements DownstreamAnalyzer {

    private final MavenSession session;

    public ReactorDownstreamAnalyzer(MavenSession session) {
        this.session = session;
    }

    @Override
    public List<MavenProject> findDownstream(MavenProject target) {
        if (target == null) {
            return Collections.emptyList();
        }
        List<MavenProject> all = session.getAllProjects();
        if (all == null || all.size() <= 1) {
            return Collections.emptyList();
        }
        return all.stream()
            .filter(p -> !Objects.equals(p, target))
            .filter(p -> dependsOn(p, target))
            .toList();
    }

    private boolean dependsOn(MavenProject consumer, MavenProject target) {
        if (consumer.getDependencies() == null) {
            return false;
        }
        return consumer.getDependencies().stream().anyMatch(d -> matches(d, target));
    }

    private boolean matches(Dependency d, MavenProject target) {
        return Objects.equals(d.getGroupId(), target.getGroupId())
            && Objects.equals(d.getArtifactId(), target.getArtifactId())
            && !"test".equals(d.getScope())
            && !d.isOptional();
    }
}
