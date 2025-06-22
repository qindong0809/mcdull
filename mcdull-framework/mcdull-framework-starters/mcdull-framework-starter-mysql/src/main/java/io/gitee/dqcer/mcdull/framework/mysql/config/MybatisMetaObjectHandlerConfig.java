package io.gitee.dqcer.mcdull.framework.mysql.config;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.TimestampEntity;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        LogHelp.debug(log, () -> "Field [createdTime、createdBy、delFlag、inactive] start insert fill ....");
        Object originalObject = metaObject.getOriginalObject();
        if (originalObject instanceof RelEntity<?>) {
            RelEntity<?> relEntity = (RelEntity<?>) originalObject;
            if (ObjUtil.isNotNull(relEntity)) {
                relEntity.setCreatedTime(UserContextHolder.getSession().getNow());
                relEntity.setUpdatedTime(UserContextHolder.getSession().getNow());
                relEntity.setDelFlag(Boolean.FALSE);
            }
        }
        if (originalObject instanceof BaseEntity<?>) {
            BaseEntity<?> baseEntity = (BaseEntity<?>) originalObject;
            if (ObjUtil.isNotNull(baseEntity)) {
                baseEntity.setCreatedTime(UserContextHolder.getSession().getNow());
                baseEntity.setCreatedBy(UserContextHolder.userId());
                baseEntity.setUpdatedTime(UserContextHolder.getSession().getNow());
                baseEntity.setUpdatedBy(UserContextHolder.userId());
                baseEntity.setDelFlag(Boolean.FALSE);
                baseEntity.setInactive(Boolean.FALSE);
            }
        }
        if (originalObject instanceof TimestampEntity<?>) {
            TimestampEntity<?> timestampEntity = (TimestampEntity<?>) originalObject;
            if (ObjUtil.isNotNull(timestampEntity)) {
                timestampEntity.setCreatedTime(UserContextHolder.getSession().getNow());
                timestampEntity.setDelFlag(Boolean.FALSE);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LogHelp.debug(log, () -> "Field [updatedTime、updatedBy] start update fill ....");
        Object originalObject = metaObject.getOriginalObject();
        if (originalObject instanceof RelEntity<?>) {
            RelEntity<?> relEntity = (RelEntity<?>) originalObject;
            if (ObjUtil.isNotNull(relEntity)) {
                relEntity.setUpdatedTime(UserContextHolder.getSession().getNow());
            }
        }
        if (originalObject instanceof BaseEntity<?>) {
            BaseEntity<?> baseEntity = (BaseEntity<?>) originalObject;
            if (ObjUtil.isNotNull(baseEntity)) {
                baseEntity.setUpdatedTime(UserContextHolder.getSession().getNow());
                baseEntity.setUpdatedBy(UserContextHolder.userId());
            }
        }
    }
}
