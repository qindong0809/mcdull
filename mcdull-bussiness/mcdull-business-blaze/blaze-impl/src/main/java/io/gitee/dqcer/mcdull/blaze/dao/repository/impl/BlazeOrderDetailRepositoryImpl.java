package io.gitee.dqcer.mcdull.blaze.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.blaze.dao.mapper.BlazeOrderDetailMapper;
import io.gitee.dqcer.mcdull.blaze.dao.repository.IBlazeOrderDetailRepository;
import io.gitee.dqcer.mcdull.blaze.domain.entity.BlazeOrderDetailEntity;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderDetailQueryDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */
@Service
public class BlazeOrderDetailRepositoryImpl extends
        ServiceImpl<BlazeOrderDetailMapper, BlazeOrderDetailEntity> implements IBlazeOrderDetailRepository {

    @Override
    public Page<BlazeOrderDetailEntity> selectPage(BlazeOrderDetailQueryDTO dto) {
        LambdaQueryWrapper<BlazeOrderDetailEntity> query = Wrappers.lambdaQuery();
        query.in(BlazeOrderDetailEntity::getResponsibleUserId, dto.getResponsibleUserIdList());
        Boolean isTalent = dto.getIsTalent();
        if (ObjUtil.isNotNull(isTalent)) {
            query.eq(BlazeOrderDetailEntity::getIsTalent, isTalent);
        }
        Integer responsibleUserId = dto.getResponsibleUserId();
        if (ObjUtil.isNotNull(responsibleUserId)) {
            query.eq(BlazeOrderDetailEntity::getResponsibleUserId, responsibleUserId);
        }
        query.in(CollUtil.isNotEmpty(dto.getOrderIdList()), BlazeOrderDetailEntity::getBlazeOrderId, dto.getOrderIdList());
        query.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    @Override
    public List<BlazeOrderDetailEntity> getByOrderId(List<Integer> orderIdList) {
        LambdaQueryWrapper<BlazeOrderDetailEntity> query = Wrappers.lambdaQuery();
        if (CollUtil.isNotEmpty(orderIdList)) {
            query.in(BlazeOrderDetailEntity::getBlazeOrderId, orderIdList);
        }
        return baseMapper.selectList(query);
    }
}