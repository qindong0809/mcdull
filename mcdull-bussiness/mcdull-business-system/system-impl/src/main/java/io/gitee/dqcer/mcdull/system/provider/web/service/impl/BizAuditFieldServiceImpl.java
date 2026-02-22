package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.BizAuditFieldEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.BizAuditFieldMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IBizAuditFieldService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BizAuditFieldServiceImpl extends BasicCurdServiceImpl<BizAuditFieldMapper, BizAuditFieldEntity>  implements IBizAuditFieldService {

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
        if (CharSequenceUtil.isNotBlank(value)) {
            LambdaQueryWrapper<BizAuditFieldEntity> query = Wrappers.lambdaQuery();
            query.like(BizAuditFieldEntity::getOldValue, value)
                .or().like(BizAuditFieldEntity::getNewValue, value);
            return baseMapper.selectList(query);
        }
        return Collections.emptyList();
    }
}
