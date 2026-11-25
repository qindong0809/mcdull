package io.gitee.dqcer.mcdull.blaze.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import io.gitee.dqcer.mcdull.blaze.web.mapper.BlazeOrderMapper;
import io.gitee.dqcer.mcdull.blaze.web.manager.IBlazeOrderRepository;
import io.gitee.dqcer.mcdull.blaze.web.manager.ICertificateRequirementsRepository;
import io.gitee.dqcer.mcdull.blaze.web.manager.ITalentCertificateRepository;
import io.gitee.dqcer.mcdull.blaze.model.entity.BlazeOrderEntity;
import io.gitee.dqcer.mcdull.blaze.model.entity.CertificateRequirementsEntity;
import io.gitee.dqcer.mcdull.blaze.model.entity.TalentCertificateEntity;
import io.gitee.dqcer.mcdull.blaze.model.dto.BlazeOrderQueryDTO;
import io.gitee.dqcer.mcdull.blaze.model.vo.BlazeOrderDetailListVO;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 订单合同 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */
@Service
public class BlazeOrderRepositoryImpl extends
        ServiceImpl<BlazeOrderMapper, BlazeOrderEntity> implements IBlazeOrderRepository {

    @Resource
    private ICertificateRequirementsRepository certificateRequirementsRepository;
    @Resource
    private ITalentCertificateRepository talentCertificateRepository;

    @Override
    public List<BlazeOrderEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<BlazeOrderEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(BlazeOrderEntity::getId, idList);
        List<BlazeOrderEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        wrapper.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return Collections.emptyList();
    }


    @Override
    public List<BlazeOrderEntity> selectList(BlazeOrderQueryDTO param) {
        LambdaQueryWrapper<BlazeOrderEntity> lambda = Wrappers.lambdaQuery();
        lambda.eq(ObjUtil.isNotNull(param.getCustomerCertId()), BlazeOrderEntity::getCustomerCertId, param.getCustomerCertId());
        lambda.eq(ObjUtil.isNotNull(param.getApprove()), BlazeOrderEntity::getApprove, param.getApprove());
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectList(lambda);
    }

    @Override
    public Map<Integer, Boolean> getMapByCustomerCertId(Collection<Integer> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
            query.in(BlazeOrderEntity::getTalentCertId, ids);
            List<BlazeOrderEntity> list = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(list)) {
                Map<Integer, Boolean> map = new HashMap<>();
                for (Integer id : ids) {
                    map.put(id, list.stream().anyMatch(entity -> entity.getCustomerCertId().equals(id)));
                }
                return map;
            }
        }
        return Map.of();
    }

    @Override
    public BlazeOrderEntity getByCustomerCertId(Integer customerCertId) {
        LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
        query.eq(BlazeOrderEntity::getCustomerCertId, customerCertId);
        List<BlazeOrderEntity> list = baseMapper.selectList(query);
        return CollUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    @Override
    public boolean existByCertificateRequirementsIdList(List<Integer> certificateRequirementsIdList) {
        if (CollUtil.isNotEmpty(certificateRequirementsIdList)) {
            LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
            query.in(BlazeOrderEntity::getCustomerCertId, certificateRequirementsIdList);
            return baseMapper.exists(query);
        }
        return false;
    }

    @Override
    public Map<Integer, Boolean> getMapByTalentCertId(Set<Integer> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
            query.in(BlazeOrderEntity::getTalentCertId, ids);
            List<BlazeOrderEntity> list = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(list)) {
                Map<Integer, Boolean> map = new HashMap<>();
                for (Integer id : ids) {
                    map.put(id, list.stream().anyMatch(entity -> entity.getTalentCertId().equals(id)));
                }
                return map;
            }
        }
        return Map.of();
    }

    @Override
    public BlazeOrderEntity getByTalentCertId(Integer talentCertId) {
        LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
        query.eq(BlazeOrderEntity::getTalentCertId, talentCertId);
        List<BlazeOrderEntity> list = baseMapper.selectList(query);
        return CollUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    @Override
    public boolean existByTalentCertificateId(List<Integer> talentCertificateIdList) {
        if (CollUtil.isNotEmpty(talentCertificateIdList)) {
            LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
            query.in(BlazeOrderEntity::getTalentCertId, talentCertificateIdList);
            return baseMapper.exists(query);
        }
        return false;
    }

    @Override
    public List<BlazeOrderDetailListVO> listBrief(BlazeOrderQueryDTO dto) {
        List<BlazeOrderEntity> orders = this.selectList(dto);
        if (CollUtil.isEmpty(orders)) {
            return List.of();
        }
        Map<Integer, String> custNameMap = certificateRequirementsRepository.list().stream()
                .collect(Collectors.toMap(CertificateRequirementsEntity::getId, CertificateRequirementsEntity::getPositionTitle, (a, b) -> a));
        Map<Integer, String> talentNameMap = talentCertificateRepository.list().stream()
                .collect(Collectors.toMap(TalentCertificateEntity::getId, TalentCertificateEntity::getPositionTitle, (a, b) -> a));
        return orders.stream().map(order -> {
            BlazeOrderDetailListVO vo = new BlazeOrderDetailListVO();
            vo.setBlazeOrderId(order.getId());
            vo.setOrderNo(order.getOrderNo());
            vo.setCustomerCertTitle(custNameMap.get(order.getCustomerCertId()));
            vo.setTalentCertTitle(talentNameMap.get(order.getTalentCertId()));
            return vo;
        }).toList();
    }

}
