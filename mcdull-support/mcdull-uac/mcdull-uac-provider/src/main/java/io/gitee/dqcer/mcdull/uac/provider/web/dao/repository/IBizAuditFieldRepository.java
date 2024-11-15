package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.BizAuditFieldEntity;

import java.util.List;
import java.util.Map;


/**
 * Biz Audit Field repository
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
public interface IBizAuditFieldRepository extends IRepository<BizAuditFieldEntity> {

    /**
     * map
     *
     * @param bizAuditIdList bizAuditIdList
     * @return {@link Map }<{@link Integer }, {@link List }<{@link BizAuditFieldEntity }>>
     */
    Map<Integer, List<BizAuditFieldEntity>> map(List<Integer> bizAuditIdList);

    /**
     * like
     *
     * @param value value
     * @return {@link List }<{@link BizAuditFieldEntity }>
     */
    List<BizAuditFieldEntity> like(String value);
}