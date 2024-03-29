package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.CustomPropertyEntity;

/**
 * @author dqcer
 */
public interface ICustomPropertyRepository extends IService<CustomPropertyEntity> {


    CustomPropertyEntity get(String code);
}
