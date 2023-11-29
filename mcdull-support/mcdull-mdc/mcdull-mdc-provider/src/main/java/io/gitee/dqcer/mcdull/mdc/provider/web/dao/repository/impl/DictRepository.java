package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictDO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.DictDAO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.IDictRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dqcer
 */
@Service
public class DictRepository extends ServiceImpl<DictDAO, DictDO> implements IDictRepository {

    @Override
    public List<DictDO> list(String selectType) {
        LambdaQueryWrapper<DictDO> query = Wrappers.lambdaQuery();
        query.eq(DictDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        query.eq(DictDO::getSelectType, selectType);
        query.orderByAsc(ListUtil.of(DictDO::getSort, DictDO::getName));
        return baseMapper.selectList(query);
    }

}
