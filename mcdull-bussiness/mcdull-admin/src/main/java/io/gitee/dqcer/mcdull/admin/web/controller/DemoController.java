package io.gitee.dqcer.mcdull.admin.web.controller;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mongodb.client.model.Filters;
import io.gitee.dqcer.mcdull.frameowrk.mongodb.DocumentQueryDTO;
import io.gitee.dqcer.mcdull.frameowrk.mongodb.MongoDBService;
import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import org.bson.conversions.Bson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 演示控制器
 *
 * @author dqcer
 * @date 2023/01/18 22:01:33
 */
@RestController
public class DemoController {

    @UnAuthorize
    @GetMapping("/")
    public Result<String> helloWord() {
        return Result.ok("hello word ");
    }

    @GetMapping("/error")
    public Result<?> unAuth404() {
        Integer.valueOf("dfd");
        return Result.ok();
    }

    @GetMapping("/user")
    public Result<UnifySession> getCurrentUserInfo() {
        return Result.ok(UserContextHolder.getSession());
    }

    @Resource
    private MongoDBService mongoDBService;

    @UnAuthorize
    @GetMapping("/db")
    public Result<UnifySession> mongodbInsert() {
        UnifySession session = new UnifySession();
        session.setLanguage("zh-CN");
        session.setUserId(123L);
        session.setNow(new Date());
        session.setTraceId(RandomUtil.uuid());
        mongoDBService.insertOrUpdate("user", session);
        return Result.ok(UserContextHolder.getSession());
    }

    @UnAuthorize
    @GetMapping("/dblist")
    public Result<List<UnifySession>> mongodbList() {
        DocumentQueryDTO queryDTO = new DocumentQueryDTO();
        queryDTO.setCollectionName("user");
        Bson filter = Filters.and(Filters.eq("userId", 123L), Filters.eq("language", "zh-CN"));
        queryDTO.setFilter(filter);
        return Result.ok(mongoDBService.queryData(queryDTO));
    }

    @UnAuthorize
    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        ServletUtil.setDownloadExcelHttpHeader(response, "测试");
//        EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(data());
    }
}
