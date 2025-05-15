package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.BizAuditQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditEntity;


/**
 * Biz Audit repository
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
public interface IBizAuditRepository extends IRepository<BizAuditEntity> {

    /**
     * 选择页面
     *
     * @param queryForm 查询表单
     * @return {@link Page }<{@link BizAuditEntity }>
     */
    Page<BizAuditEntity> selectPage(BizAuditQueryDTO queryForm);
}