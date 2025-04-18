package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.HelpDocRelationEntity;

import java.util.List;

/**
 * Help doc relation repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IHelpDocRelationRepository extends IRepository<HelpDocRelationEntity> {

    /**
     * list by relation id
     *
     * @param relationId relation id
     * @return {@link List}<{@link HelpDocRelationEntity}>
     */
    List<HelpDocRelationEntity> listByRelationId(Integer relationId);
}