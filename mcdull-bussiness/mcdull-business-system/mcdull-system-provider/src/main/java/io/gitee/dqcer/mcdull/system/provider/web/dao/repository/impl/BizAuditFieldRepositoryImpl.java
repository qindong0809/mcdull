package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditFieldEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.BizAuditFieldMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IBizAuditFieldRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * biz audit field 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class BizAuditFieldRepositoryImpl extends
        CrudRepository<BizAuditFieldMapper, BizAuditFieldEntity> implements IBizAuditFieldRepository {

    @Override
    public Map<Integer, List<BizAuditFieldEntity>> map(List<Integer> bizAuditIdList) {
        if (CollUtil.isNotEmpty(bizAuditIdList)) {
            LambdaQueryWrapper<BizAuditFieldEntity> query = Wrappers.lambdaQuery();
            query.in(BizAuditFieldEntity::getBizAuditId, bizAuditIdList);
            List<BizAuditFieldEntity> list = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().collect(Collectors.groupingBy(BizAuditFieldEntity::getBizAuditId));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public List<BizAuditFieldEntity> like(String value) {
        if (StrUtil.isNotBlank(value)) {
            LambdaQueryWrapper<BizAuditFieldEntity> query = Wrappers.lambdaQuery();
            query.like(BizAuditFieldEntity::getOldValue, value)
                    .or().like(BizAuditFieldEntity::getNewValue, value);
            return baseMapper.selectList(query);
        }
        return Collections.emptyList();
    }
}
