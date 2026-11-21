package io.gitee.dqcer.mcdull.common.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户引用提取（公共逻辑）。
 * <p>
 * 原 {@code UserApiDef.getMap(Collection<?>)} 依赖实体反射，无法跨服务序列化。
 * 调用方先用本工具从实体中取出 createdBy/updatedBy，再远程调用 {@code UserApiDef.getNameMap}。
 */
public class UserRefUtil {

    private UserRefUtil() {
    }

    public static List<Integer> collectUserIds(Collection<? extends BaseEntity<?>> entityList) {
        Set<Integer> ids = new HashSet<>();
        if (CollUtil.isEmpty(entityList)) {
            return new ArrayList<>();
        }
        for (BaseEntity<?> entity : entityList) {
            if (ObjUtil.isNull(entity)) {
                continue;
            }
            if (ObjUtil.isNotNull(entity.getCreatedBy())) {
                ids.add(entity.getCreatedBy());
            }
            if (ObjUtil.isNotNull(entity.getUpdatedBy())) {
                ids.add(entity.getUpdatedBy());
            }
        }
        return new ArrayList<>(ids);
    }
}
