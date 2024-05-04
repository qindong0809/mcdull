package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocCatalogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocRelationEntity;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface IHelpDocRelationRepository extends IService<HelpDocRelationEntity>  {

    List<HelpDocRelationEntity> listByRelationId(Long relationId);
}