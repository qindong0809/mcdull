package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EnterpriseQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OaEnterpriseEntity;

/**
* @author dqcer
* @since 2024-04-29
*/
public interface IOaEnterpriseRepository extends IService<OaEnterpriseEntity>  {


    Page<OaEnterpriseEntity> selectPage(EnterpriseQueryDTO dto);
}