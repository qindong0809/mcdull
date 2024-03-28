package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictTypeDO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.DictTypeMapper;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.IDictTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dqcer
 */
@Service
public class DictTypeRepositoryImpl extends ServiceImpl<DictTypeMapper, DictTypeDO> implements IDictTypeRepository {

    @Override
    public List<DictTypeDO> list(String type) {
        LambdaQueryWrapper<DictTypeDO> query = Wrappers.lambdaQuery();
        query.eq(DictTypeDO::getDictType, type);
        return baseMapper.selectList(query);
    }

}
