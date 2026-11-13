package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.HelpDocRelationEntity;

import java.util.List;

public interface IHelpDocRelationService extends IRepository<HelpDocRelationEntity> {

    /**
     * list by relation id
     *
     * @param relationId relation id
     * @return {@link List}<{@link HelpDocRelationEntity}>
     */
    List<HelpDocRelationEntity> listByRelationId(Integer relationId);
}
