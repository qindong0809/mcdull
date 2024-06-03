package io.gitee.dqcer.mcdull.framework.web.version;

import java.util.Properties;

/**
 *
 * @author qcer
 * @since 2024/05/23
 */
public interface IVersionInfoComponent {

    /**
     * 获取jar当前构建信息
     *
     * @return {@link Properties}
     */
    Properties getJarCurrentBuildInfo();

    /**
     * 获取git当前提交信息
     *
     * @return {@link Properties}
     */
    Properties getGitCurrentCommitInfo();
}
