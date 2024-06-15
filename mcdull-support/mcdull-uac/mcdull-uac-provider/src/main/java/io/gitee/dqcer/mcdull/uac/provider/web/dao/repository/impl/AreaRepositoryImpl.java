package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.AreaEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.AreaMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IAreaRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * Area 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class AreaRepositoryImpl extends
        ServiceImpl<AreaMapper, AreaEntity> implements IAreaRepository {

    @Override
    public List<AreaEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<AreaEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(AreaEntity::getId, idList);
        List<AreaEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }


    @Override
    public Page<AreaEntity> selectPage(AreaQueryDTO param) {
        LambdaQueryWrapper<AreaEntity> lambda = Wrappers.lambdaQuery();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            // TODO 组装查询条件
        }
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

}
