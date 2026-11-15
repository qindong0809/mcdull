package io.gitee.dqcer.mcdull.devtools.notify.git;

import io.gitee.dqcer.mcdull.devtools.notify.model.GitContext;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Git 上下文四层降级解析：
 * <ol>
 *   <li>MavenSession 顶层 basedir + JGit findGitDir —— 主流路径</li>
 *   <li>target/classes/git.properties —— 无 .git 时兜底（tar 包构建）</li>
 *   <li>完全降级：返回 GitContext.empty()，消息仍发但无变更摘要</li>
 * </ol>
 * 任何一层异常都不 rethrow，只 warn 到 Maven 日志。
 *
 * @author dqcer
 */
public class GitContextResolver {

    private static final int MAX_RECENT_COMMITS = 3;

    private final Log log;

    public GitContextResolver(Log log) {
        this.log = log;
    }

    public GitContext resolve(MavenSession session) {
        MavenProject top = session.getTopLevelProject();
        GitContext byJgit = tryJgit(top);
        if (!byJgit.isEmpty()) {
            return byJgit;
        }
        GitContext byProperties = tryGitProperties(top);
        if (!byProperties.isEmpty()) {
            return byProperties;
        }
        log.warn("[mcdull-notify] 未取到 git 信息，通知内容降级");
        return GitContext.empty();
    }

    private GitContext tryJgit(MavenProject top) {
        File basedir = top == null ? null : top.getBasedir();
        if (basedir == null) {
            return GitContext.empty();
        }
        Repository repository = null;
        try {
            repository = new FileRepositoryBuilder()
                .readEnvironment()
                .findGitDir(basedir)
                .build();
            if (repository.getDirectory() == null) {
                return GitContext.empty();
            }
            String branch = repository.getBranch();
            String remoteUrl = repository.getConfig() == null
                ? null
                : repository.getConfig().getString("remote", "origin", "url");

            List<GitContext.Commit> commits = new ArrayList<>(MAX_RECENT_COMMITS);
            try (Git git = new Git(repository)) {
                Iterable<RevCommit> it = git.log().setMaxCount(MAX_RECENT_COMMITS).call();
                for (RevCommit c : it) {
                    commits.add(new GitContext.Commit(
                        c.getName().substring(0, Math.min(7, c.getName().length())),
                        c.getShortMessage(),
                        c.getAuthorIdent() == null ? "unknown" : c.getAuthorIdent().getName()));
                }
            }
            return GitContext.of(branch, remoteUrl, commits);
        } catch (Exception e) {
            log.warn("[mcdull-notify] JGit 解析失败，走兜底: {}", e);
            return GitContext.empty();
        } finally {
            if (repository != null) {
                repository.close();
            }
        }
    }

    private GitContext tryGitProperties(MavenProject top) {
        if (top == null) {
            return GitContext.empty();
        }
        Path p = top.getBasedir().toPath().resolve("target/classes/git.properties");
        if (!Files.exists(p)) {
            return GitContext.empty();
        }
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(p)) {
            props.load(in);
        } catch (IOException e) {
            log.warn("[mcdull-notify] 读取 git.properties 失败: {}", e);
            return GitContext.empty();
        }
        String branch = props.getProperty("git.branch");
        String sha = props.getProperty("git.commit.id.abbrev");
        String user = props.getProperty("git.commit.user.name");
        String subject = props.getProperty("git.commit.message.short");
        String remote = props.getProperty("git.remote.origin.url");
        List<GitContext.Commit> commits = sha == null
            ? List.of()
            : List.of(new GitContext.Commit(sha, subject, user));
        return GitContext.of(branch, remote, commits);
    }

    /**
     * 保留：显式 repo 路径入口，为将来 settings.xml 里的 override 预留。
     */
    GitContext openRepoAt(File dotGitParent) {
        try (Repository r = new RepositoryBuilder().findGitDir(dotGitParent).build()) {
            if (r.getDirectory() == null) {
                return GitContext.empty();
            }
            return GitContext.of(r.getBranch(), null, List.of());
        } catch (IOException e) {
            return GitContext.empty();
        }
    }
}
