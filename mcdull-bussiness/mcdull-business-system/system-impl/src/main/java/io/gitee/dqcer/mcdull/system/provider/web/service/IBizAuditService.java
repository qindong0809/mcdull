package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.OperationTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.BizAuditQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.BizAuditVO;

import java.util.Date;

/**
 * Biz audit Service
 *
 * @author dqcer
 * @since 2024/7/25 9:16
 */

public interface IBizAuditService extends IRepository<BizAuditEntity> {

    /**
     * 插入
     *
     * @param bizTypeCode       业务类型代码
     * @param operationTypeEnum 操作类型 enum
     * @param bizIndex          bizIndex
     * @param bizId             业务 ID
     * @param comment           comment
     * @param operator          operator
     * @param operationTime     操作时间
     * @param ext               ext
     */
    Integer insert(String bizTypeCode, OperationTypeEnum operationTypeEnum, String bizIndex, Integer bizId, String comment, String operator, Date operationTime, String ext);

    /**
     * Query 页面
     *
     * @param queryForm 查询表单
     * @return {@link PagedVO }<{@link BizAuditVO }>
     */
    PagedVO<BizAuditVO> queryPage(BizAuditQueryDTO queryForm);

    /**
     * 导出数据
     *
     * @param dto DTO
     */
    boolean exportData(BizAuditQueryDTO dto);


    /**
     * Save by add enum （通过添加枚举保存）
     *
     * @param bizIndex    业务指数
     * @param bizId       业务 ID
     * @param auditBean   审计 Bean
     */
    <T extends Audit> void saveByAddEnum(String bizIndex, Integer bizId, T auditBean);

    /**
     * Save by update 枚举
     *
     * @param bizIndex     业务指数
     * @param bizId        bizId
     * @param oldAuditBean oldAuditBean
     * @param newAuditBean newAuditBean
     */
    <T extends Audit> void saveByUpdateEnum(String bizIndex, Integer bizId, T oldAuditBean, T newAuditBean);

    /**
     * Save by delete 枚举
     *
     * @param bizIndex  业务指数
     * @param bizId     bizId
     * @param reason reason
     */
    void saveByDeleteEnum(String bizIndex, Integer bizId, String reason);

    /**
     * Save by status 枚举
     *
     * @param bizIndex bizIndex
     * @param bizId    业务 ID
     * @param active   active
     * @param reason   原因
     */
    void saveByStatusEnum(String bizIndex, Integer bizId, boolean active, String reason);
}
