package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.enums.InactiveEnum;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.RoleMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * role repository impl
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class RoleRepositoryImpl
        extends CrudRepository<RoleMapper, RoleEntity> implements IRoleRepository {

    @Override
    public Integer insert(RoleEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public Map<Integer, List<RoleEntity>> roleListMap(Map<Integer, List<Integer>> userRoleMap) {
        Map<Integer, List<RoleEntity>> resultMap = new HashMap<>(userRoleMap.size());
        if (MapUtil.isNotEmpty(userRoleMap)) {
            Set<Integer> idList = userRoleMap.values().stream()
                    .flatMap(Collection::stream).collect(Collectors.toSet());

            LambdaQueryWrapper<RoleEntity> query = Wrappers.lambdaQuery();
            query.eq(RoleEntity::getInactive, InactiveEnum.FALSE.getCode());
            query.in(RoleEntity::getId, idList);
            List<RoleEntity> list = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(list)) {
                Map<Integer, RoleEntity> map = list.stream()
                        .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
                for (Map.Entry<Integer, List<Integer>> entry : userRoleMap.entrySet()) {
                    List<Integer> roleIdList = entry.getValue();
                    List<RoleEntity> roleList = roleIdList.stream().map(map::get)
                            .filter(ObjUtil::isNotEmpty).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(roleList)) {
                        resultMap.put(entry.getKey(), roleList);
                    }
                }
            }
        }
        return resultMap;
    }

    @Override
    public boolean delete(Integer id, String reason) {
        return this.removeById(id);
    }

    @Override
    public boolean toggleStatus(Integer id, boolean inactive) {
        RoleEntity role = new RoleEntity();
        role.setId(id);
        role.setInactive(inactive);
        return baseMapper.updateById(role) > 0;
    }
}
