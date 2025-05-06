package io.gitee.dqcer.mcdull.blaze.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.blaze.dao.mapper.BlazeOrderMapper;
import io.gitee.dqcer.mcdull.blaze.dao.repository.IBlazeOrderRepository;
import io.gitee.dqcer.mcdull.blaze.domain.entity.BlazeOrderEntity;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderQueryDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * 订单合同 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */
@Service
public class BlazeOrderRepositoryImpl extends
        ServiceImpl<BlazeOrderMapper, BlazeOrderEntity> implements IBlazeOrderRepository {

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
    public Page<BlazeOrderEntity> selectPage(BlazeOrderQueryDTO param) {
        LambdaQueryWrapper<BlazeOrderEntity> lambda = Wrappers.lambdaQuery();
        lambda.eq(ObjUtil.isNotNull(param.getApprove()), BlazeOrderEntity::getApprove, param.getApprove());
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

}