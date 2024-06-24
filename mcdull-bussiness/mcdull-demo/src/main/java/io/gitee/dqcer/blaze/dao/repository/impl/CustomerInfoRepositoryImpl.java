package io.gitee.dqcer.blaze.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.blaze.dao.mapper.CustomerInfoMapper;
import io.gitee.dqcer.blaze.dao.repository.ICustomerInfoRepository;
import io.gitee.dqcer.blaze.domain.entity.CustomerInfoEntity;
import io.gitee.dqcer.blaze.domain.form.CustomerInfoQueryDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * 企业信息 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */
@Service
public class CustomerInfoRepositoryImpl extends
        ServiceImpl<CustomerInfoMapper, CustomerInfoEntity> implements ICustomerInfoRepository {

    @Override
    public List<CustomerInfoEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<CustomerInfoEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CustomerInfoEntity::getId, idList);
        List<CustomerInfoEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        wrapper.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return Collections.emptyList();
    }


    @Override
    public Page<CustomerInfoEntity> selectPage(CustomerInfoQueryDTO param) {
        LambdaQueryWrapper<CustomerInfoEntity> lambda = Wrappers.lambdaQuery();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            // TODO 组装查询条件
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

}