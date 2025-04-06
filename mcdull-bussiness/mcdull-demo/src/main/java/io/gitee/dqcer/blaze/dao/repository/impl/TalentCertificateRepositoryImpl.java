package io.gitee.dqcer.blaze.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.blaze.dao.mapper.TalentCertificateMapper;
import io.gitee.dqcer.blaze.dao.repository.ITalentCertificateRepository;
import io.gitee.dqcer.blaze.domain.entity.TalentCertificateEntity;
import io.gitee.dqcer.blaze.domain.form.TalentCertificateQueryDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * 证书需求表 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
@Service
public class TalentCertificateRepositoryImpl extends
        ServiceImpl<TalentCertificateMapper, TalentCertificateEntity> implements ITalentCertificateRepository {

    @Override
    public List<TalentCertificateEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<TalentCertificateEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TalentCertificateEntity::getId, idList);
        List<TalentCertificateEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        wrapper.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return Collections.emptyList();
    }


    @Override
    public Page<TalentCertificateEntity> selectPage(TalentCertificateQueryDTO param) {
        LambdaQueryWrapper<TalentCertificateEntity> lambda = Wrappers.lambdaQuery();
        lambda.eq(ObjUtil.isNotNull(param.getCertificateLevel()), TalentCertificateEntity::getCertificateLevel, param.getCertificateLevel());
        lambda.eq(ObjUtil.isNotNull(param.getSpecialty()), TalentCertificateEntity::getSpecialty, param.getSpecialty());
        lambda.eq(ObjUtil.isNotNull(param.getBiddingExit()), TalentCertificateEntity::getBiddingExit, param.getBiddingExit());
        lambda.eq(ObjUtil.isNotNull(param.getThreePersonnel()), TalentCertificateEntity::getThreePersonnel, param.getThreePersonnel());
        lambda.eq(ObjUtil.isNotNull(param.getSocialSecurityRequirement()), TalentCertificateEntity::getSocialSecurityRequirement, param.getSocialSecurityRequirement());
        lambda.eq(ObjUtil.isNotNull(param.getInitialOrTransfer()), TalentCertificateEntity::getInitialOrTransfer, param.getInitialOrTransfer());
        lambda.eq(ObjUtil.isNotNull(param.getCertificateStatus()), TalentCertificateEntity::getCertificateStatus, param.getCertificateStatus());
        lambda.eq(ObjUtil.isNotNull(param.getApprove()), TalentCertificateEntity::getApprove, param.getApprove());
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

}