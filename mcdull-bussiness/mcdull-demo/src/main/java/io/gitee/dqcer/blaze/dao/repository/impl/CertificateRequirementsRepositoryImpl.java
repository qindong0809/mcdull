package io.gitee.dqcer.blaze.dao.repository.impl;

import io.gitee.dqcer.blaze.dao.mapper.CertificateRequirementsMapper;
import io.gitee.dqcer.blaze.dao.repository.ICertificateRequirementsRepository;
import io.gitee.dqcer.blaze.domain.entity.CertificateRequirementsEntity;

import java.util.List;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsQueryDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;


/**
 * 证书需求表 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
@Service
public class CertificateRequirementsRepositoryImpl extends
        ServiceImpl<CertificateRequirementsMapper, CertificateRequirementsEntity> implements ICertificateRequirementsRepository {

    @Override
    public List<CertificateRequirementsEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<CertificateRequirementsEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CertificateRequirementsEntity::getId, idList);
        List<CertificateRequirementsEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        wrapper.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return Collections.emptyList();
    }


    @Override
    public Page<CertificateRequirementsEntity> selectPage(CertificateRequirementsQueryDTO param) {
        LambdaQueryWrapper<CertificateRequirementsEntity> lambda = Wrappers.lambdaQuery();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            // TODO 组装查询条件
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

}