package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.CustomPropertyDO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.CustomPropertyMapper;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.ICustomPropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dqcer
 */
@Service
public class CustomPropertyRepositoryImpl extends ServiceImpl<CustomPropertyMapper, CustomPropertyDO>
        implements ICustomPropertyRepository {

    @Override
    public CustomPropertyDO get(String code) {
        LambdaQueryWrapper<CustomPropertyDO> query = Wrappers.lambdaQuery();
        query.eq(CustomPropertyDO::getCode, code);
        List<CustomPropertyDO> list = baseMapper.selectList(query);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
