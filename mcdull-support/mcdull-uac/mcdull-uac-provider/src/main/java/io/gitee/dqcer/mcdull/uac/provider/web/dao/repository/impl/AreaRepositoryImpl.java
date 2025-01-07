package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;


import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.AreaEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.AreaMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IAreaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        CrudRepository<AreaMapper, AreaEntity> implements IAreaRepository {

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
            lambda.and(i->i.like(AreaEntity::getName, keyword).or()
                    .like(AreaEntity::getFullname, keyword));
        }
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    @Override
    public List<AreaEntity> getByAreaType(int areaType) {
        LambdaQueryWrapper<AreaEntity> query = Wrappers.lambdaQuery();
        query.eq(AreaEntity::getAreaType, areaType);
        return baseMapper.selectList(query);
    }

    @Override
    public List<AreaEntity> getByPid(Integer pid) {
        LambdaQueryWrapper<AreaEntity> query = Wrappers.lambdaQuery();
        query.eq(AreaEntity::getPid, pid);
        return baseMapper.selectList(query);
    }

    @Override
    public AreaEntity getCode(String code) {
        LambdaQueryWrapper<AreaEntity> query = Wrappers.lambdaQuery();
        query.eq(AreaEntity::getCode, code);
        return baseMapper.selectOne(query);
    }

    @Override
    public List<AreaEntity> listByCode(ArrayList<String> codeList) {
        if (ObjUtil.isNotNull(codeList)) {
            LambdaQueryWrapper<AreaEntity> query = Wrappers.lambdaQuery();
            query.in(AreaEntity::getCode, codeList);
            return baseMapper.selectList(query);
        }
        return Collections.emptyList();
    }

}
