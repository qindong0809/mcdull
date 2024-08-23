package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.ProductionScheduleMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IProductionScheduleRepository;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.entity.ProductionScheduleEntity;
import io.gitee.dqcer.mcdull.uac.provider.xcr.domain.form.ProductionScheduleQueryDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * 生产进度表 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2024-08-23 13:55:13
 */
@Service
public class ProductionScheduleRepositoryImpl extends
        ServiceImpl<ProductionScheduleMapper, ProductionScheduleEntity> implements IProductionScheduleRepository {

    @Override
    public List<ProductionScheduleEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<ProductionScheduleEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ProductionScheduleEntity::getId, idList);
        List<ProductionScheduleEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        wrapper.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return Collections.emptyList();
    }


    @Override
    public Page<ProductionScheduleEntity> selectPage(ProductionScheduleQueryDTO param) {
        LambdaQueryWrapper<ProductionScheduleEntity> lambda = Wrappers.lambdaQuery();
        String keyword = param.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.and(i->i.like(ProductionScheduleEntity::getCustomer, keyword)
                    .or().like(ProductionScheduleEntity::getAddress, keyword)
                    .or().like(ProductionScheduleEntity::getContactInfo, keyword)
                    .or().like(ProductionScheduleEntity::getProductName, keyword));
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

}