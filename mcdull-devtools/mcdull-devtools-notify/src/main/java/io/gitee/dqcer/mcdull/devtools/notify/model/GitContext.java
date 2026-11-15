package io.gitee.dqcer.mcdull.devtools.notify.model;

import java.util.Collections;
import java.util.List;

/**
 * Git 上下文解析结果。不可变。
 * <p>
 * 三层降级都返回同一类型，"未取到"用字段为 null / 空集合表达，不返回 null。
 *
 * @author dqcer
 */
public final class GitContext {

    private final String branch;

    private final String remoteUrl;

    private final List<Commit> recentCommits;

    /**
     * true 表示未能获取任何 git 信息（tar 包构建等），消息仍会发但内容降级为薄版。
     */
    private final boolean empty;

    private GitContext(String branch, String remoteUrl, List<Commit> commits, boolean empty) {
        this.branch = branch;
        this.remoteUrl = remoteUrl;
        this.recentCommits = commits == null ? Collections.emptyList() : Collections.unmodifiableList(commits);
        this.empty = empty;
    }

    public static GitContext empty() {
        return new GitContext(null, null, Collections.emptyList(), true);
    }

    public static GitContext of(String branch, String remoteUrl, List<Commit> commits) {
        return new GitContext(branch, remoteUrl, commits, false);
    }

    public String getBranch() {
        return branch;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public List<Commit> getRecentCommits() {
        return recentCommits;
    }

    public boolean isEmpty() {
        return empty;
    }

    /**
     * 单条 commit 摘要。
     */
    public record Commit(String shortSha, String subject, String authorName) {
    }
}
