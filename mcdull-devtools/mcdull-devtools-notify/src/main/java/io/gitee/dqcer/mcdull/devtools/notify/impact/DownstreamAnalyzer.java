package io.gitee.dqcer.mcdull.devtools.notify.impact;

import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * 计算给定 facade 模块的下游消费者清单。
 * <p>
 * 抽成接口是为将来跨仓库场景（Nexus 反查 API / 订阅模式）预留扩展点，
 * MVP 只提供 Reactor 实现。
 *
 * @author dqcer
 */
public interface DownstreamAnalyzer {

    List<MavenProject> findDownstream(MavenProject target);
}
