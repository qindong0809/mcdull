package com.dqcer.mcdull.framework.mysql.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.dqcer.framework.base.storage.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * mybatis 数据源处理 配置
 *
 * @author dqcer
 * @version 2021/08/21 20:08:55
 */
public class MybatisMetaObjectHandlerConfig implements MetaObjectHandler {

    private static final Logger log = LoggerFactory.getLogger(MybatisMetaObjectHandlerConfig.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createdTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "createdBy", () -> UserContextHolder.getSession().getUserId(), Long.class);
        this.strictInsertFill(metaObject, "delFlag", () -> 1, Integer.class);
        this.strictInsertFill(metaObject, "status", () -> 1, Integer.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedTime", () -> new Date(), Date.class);
        this.strictUpdateFill(metaObject, "updatedBy", () -> UserContextHolder.getSession().getUserId(), Long.class);
    }
}
