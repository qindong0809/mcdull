package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.CustomPropertyDO;

/**
 * @author dqcer
 */
public interface ICustomPropertyRepository extends IService<CustomPropertyDO> {


    CustomPropertyDO get(String code);
}
