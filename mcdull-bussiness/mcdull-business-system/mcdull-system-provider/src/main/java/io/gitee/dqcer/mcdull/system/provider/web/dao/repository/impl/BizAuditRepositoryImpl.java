package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.system.provider.model.dto.BizAuditQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.BizAuditMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IBizAuditRepository;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import org.springframework.stereotype.Service;


/**
 * biz audit 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class BizAuditRepositoryImpl extends
        CrudRepository<BizAuditMapper, BizAuditEntity> implements IBizAuditRepository {

    @Override
    public Page<BizAuditEntity> selectPage(BizAuditQueryDTO queryForm) {
        LambdaQueryWrapper<BizAuditEntity> query = Wrappers.lambdaQuery();
        query.like(StrUtil.isNotBlank(queryForm.getKeyword()), BizAuditEntity::getComment, queryForm.getKeyword());
        query.eq(queryForm.getOperation() != null, BizAuditEntity::getOperation, queryForm.getOperation());
        query.eq(StrUtil.isNotBlank(queryForm.getBizTypeCode()), BizAuditEntity::getBizTypeCode, queryForm.getBizTypeCode());
        query.eq(StrUtil.isNotBlank(queryForm.getOperator()), BizAuditEntity::getOperator, queryForm.getOperator());
        query.eq(StrUtil.isNotBlank(queryForm.getBizIndex()), BizAuditEntity::getBizId, queryForm.getBizIndex());
        query.orderByDesc(BizAuditEntity::getOperationTime);
        return baseMapper.selectPage(new Page<>(queryForm.getPageNum(), queryForm.getPageSize()), query);
    }
}
