package io.gitee.dqcer.mcdull.admin.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class JGitUtil {

    public static void main(String[] args) throws Exception {
        String localPath = "D:\\temp";    //本地仓库位置`

        String remotePath = "https://gitee.com/dqcer/mcdull.git";  //远端仓库URL
        String branch = "master";   //克隆目标分支
        String userName = "";  //远端仓库用户名
        String password = "";  //远端仓库密码密码

        //克隆
//        clone(localPath, remotePath, branch, userName, password);

//        byte[] bytes = FileUtil.readBytes(localPath + "\\sql\\common.sql");
//        System.out.println(StrUtil.str(bytes, CharsetUtil.CHARSET_UTF_8));

        gitCommitAndPush(localPath, "\\mcdull", "test", userName, password);

    }

    public static void clone(String localPath, String remotePath, String userName, String password) throws GitAPIException {
        clone(localPath, remotePath, "master", userName, password);
    }

    public static void clone(String localPath, String remotePath, String branch, String userName, String password) throws GitAPIException {
        CredentialsProvider provider = new UsernamePasswordCredentialsProvider(userName, password);
        File localGitFile = new File(localPath);
        Git git = Git.cloneRepository()
                .setURI(remotePath)
                .setDirectory(localGitFile)
                .setCredentialsProvider(provider)
                .setCloneSubmodules(true)
                .setBranch(branch)
                .call();
        git.getRepository().close();
        git.close();
    }


    public static void gitCommitAndPush(String localPath, String projectName, String commitMessage, String username, String password) throws IOException, GitAPIException {
        // 找到本地的git路径，一般在隐藏.git文件夹下
        Git git = new Git(new FileRepository(localPath + projectName + "\\.git"));
        // 提交代码
        git.add().addFilepattern(".").call();
        git.commit().setAll(true).setMessage(commitMessage).call();
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new
                UsernamePasswordCredentialsProvider(username, password);
        //git仓库地址
        git.push().setRemote("origin").setCredentialsProvider(usernamePasswordCredentialsProvider).call();
    }

    public static String readFile(String sqlPath) {
        boolean isFile = FileUtil.isFile(sqlPath);
        if (!isFile) {
            throw new IllegalArgumentException();
        }
        byte[] bytes = FileUtil.readBytes(sqlPath);
        return StrUtil.str(bytes, CharsetUtil.CHARSET_UTF_8);
    }

    public static void appendNewSqlScriptFile(String initScriptPath, String newScriptPath) {
        boolean isFile = FileUtil.isFile(initScriptPath);
        if (!isFile) {
            throw new IllegalArgumentException();
        }
        byte[] bytes = StrUtil.utf8Bytes(readFile(newScriptPath));
        FileUtil.writeBytes(bytes, FileUtil.file(initScriptPath), 0, bytes.length, true);
    }

}
