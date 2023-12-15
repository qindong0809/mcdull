package io.gitee.dqcer.mcdull.admin.web.controller;

import cn.hutool.core.io.FileUtil;
import io.gitee.dqcer.mcdull.CodeGenerator;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.oss.factory.OssClient;
import io.gitee.dqcer.mcdull.framework.oss.component.OssFactory;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 演示控制器
 *
 * @author dqcer
 * @since 2023/01/18 22:01:33
 */
@RestController
public class DemoController {

    @UnAuthorize
    @GetMapping("/")
    public Result<String> helloWord() {
        return Result.success("hello word ");
    }

    @GetMapping("/error")
    public Result<?> unAuth404() {
        Integer.valueOf("dfd");
        return Result.success();
    }

    @GetMapping("/user")
    public Result<UnifySession> getCurrentUserInfo() {
        return Result.success(UserContextHolder.getSession());
    }

//    @Resource
//    private MongoDBService mongoDBService;

//    @UnAuthorize
//    @GetMapping("/db")
//    public Result<UnifySession> mongodbInsert() {
//        UnifySession session = new UnifySession();
//        session.setLanguage("zh-CN");
//        session.setUserId(123L);
//        session.setNow(new Date());
//        session.setTraceId(RandomUtil.uuid());
//        mongoDBService.insertOrUpdate("user", session);
//        return Result.ok(UserContextHolder.getSession());
//    }
//
//    @UnAuthorize
//    @GetMapping("/dblist")
//    public Result<List<UnifySession>> mongodbList() {
//        DocumentQueryDTO queryDTO = new DocumentQueryDTO();
//        queryDTO.setCollectionName("user");
//        Bson filter = Filters.and(Filters.eq("userId", 123L), Filters.eq("language", "zh-CN"));
//        queryDTO.setFilter(filter);
//        return Result.ok(mongoDBService.queryData(queryDTO, UnifySession.class));
//    }

    public static void main(String[] args) {
        demoCodeGenerator();
    }
    /**
     * 演示代码生成器
     */
    public static void demoCodeGenerator() {
        CodeGenerator.run("database", "database_config_env", true);
    }

    @UnAuthorize
    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        ServletUtil.setDownloadExcelHttpHeader(response, "测试");
//        EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(data());
    }

    @Resource
    private OssFactory ossFactory;

    @UnAuthorize
    @GetMapping("upload")
    public void upload() {
        OssClient instance = ossFactory.getInstance();
        byte[] bytes = FileUtil.readBytes("D:/temp/1.pdf");
        instance.upload(bytes, "demo.pdf", "mcdull");
    }
}
