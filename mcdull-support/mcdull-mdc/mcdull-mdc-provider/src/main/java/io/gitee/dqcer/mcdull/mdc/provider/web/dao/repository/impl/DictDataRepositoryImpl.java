package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictDataDO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.DictDataMapper;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.IDictDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dqcer
 */
@Service
public class DictDataRepositoryImpl extends ServiceImpl<DictDataMapper, DictDataDO> implements IDictDataRepository {

    @Override
    public List<DictDataDO> list(String type) {
        LambdaQueryWrapper<DictDataDO> query = Wrappers.lambdaQuery();
        query.eq(DictDataDO::getDictType, type);
        return baseMapper.selectList(query);
    }

}
