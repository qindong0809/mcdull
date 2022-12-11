package com.dqcer.mcdull.uac.provider.web.dao.repository;

import com.dqcer.framework.base.entity.BaseDO;
import com.dqcer.framework.base.enums.StatusEnum;
import com.dqcer.framework.base.storage.UserContextHolder;

public interface IBaseRepository<T extends BaseDO> {

    default T convert(T entity) {
        entity.setStatus(StatusEnum.ENABLE.getCode());
        entity.setCreatedBy(UserContextHolder.getSession().getUserId());
        return entity;
    }
}
