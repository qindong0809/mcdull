package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.database.BackInstanceEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.InstanceBackMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IBackInstanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
* Instance back 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class BackInstanceRepositoryImpl extends ServiceImpl<InstanceBackMapper, BackInstanceEntity>  implements IBackInstanceRepository {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<BackInstanceEntity> listByBackId(Long backId) {
        LambdaQueryWrapper<BackInstanceEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BackInstanceEntity::getBackId, backId);
        List<BackInstanceEntity> list = baseMapper.selectList(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        return Collections.emptyList();
    }
}