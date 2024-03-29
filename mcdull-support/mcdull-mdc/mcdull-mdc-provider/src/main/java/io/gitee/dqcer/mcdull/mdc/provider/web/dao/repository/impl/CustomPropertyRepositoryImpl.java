package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.CustomPropertyEntity;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.CustomPropertyMapper;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.ICustomPropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dqcer
 */
@Service
public class CustomPropertyRepositoryImpl extends ServiceImpl<CustomPropertyMapper, CustomPropertyEntity>
        implements ICustomPropertyRepository {

    @Override
    public CustomPropertyEntity get(String code) {
        LambdaQueryWrapper<CustomPropertyEntity> query = Wrappers.lambdaQuery();
        query.eq(CustomPropertyEntity::getCode, code);
        List<CustomPropertyEntity> list = baseMapper.selectList(query);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
