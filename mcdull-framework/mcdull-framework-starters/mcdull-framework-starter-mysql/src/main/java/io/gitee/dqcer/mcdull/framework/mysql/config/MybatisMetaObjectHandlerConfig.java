package io.gitee.dqcer.mcdull.framework.mysql.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 数据库字段自动填充配置
 *
 * @author dqcer
 * @since 2021/08/21 20:08:55
 */
public class MybatisMetaObjectHandlerConfig implements MetaObjectHandler {

    private static final Logger log = LoggerFactory.getLogger(MybatisMetaObjectHandlerConfig.class);

    /**
     * 插入填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.debug("Field [createdTime、createdBy、delFlag] start insert fill ....");
        }
        this.strictInsertFill(metaObject, "createdTime", () -> UserContextHolder.getSession().getNow(), Date.class);
        this.strictInsertFill(metaObject, "createdBy", UserContextHolder::currentUserId, Long.class);
        this.strictInsertFill(metaObject, "delFlag", DelFlayEnum.NORMAL::getCode, Integer.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.debug("Field [updatedTime、updatedBy] start update fill ....");
        }
        this.strictUpdateFill(metaObject, "updatedTime", () -> UserContextHolder.getSession().getNow(), Date.class);
        this.strictUpdateFill(metaObject, "updatedBy", UserContextHolder::currentUserId, Long.class);
    }
}
