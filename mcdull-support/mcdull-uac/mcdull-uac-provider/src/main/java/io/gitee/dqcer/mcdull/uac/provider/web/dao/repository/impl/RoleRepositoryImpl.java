package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.RoleMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRoleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 角色 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class RoleRepositoryImpl extends ServiceImpl<RoleMapper, RoleDO> implements IRoleRepository {

    @Resource
    private IUserRoleRepository userRoleRepository;

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    @Override
    public Page<RoleDO> selectPage(RoleLiteDTO dto) {
        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(RoleDO::getName, keyword));
        }
        query.orderByDesc(BaseDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    @Override
    public Long insert(RoleDO entity) {
        entity.setDelFlag(DelFlayEnum.NORMAL.getCode());
        entity.setCreatedBy(UserContextHolder.currentUserId());
        entity.setCreatedTime(new Date());
        int row = baseMapper.insert(entity);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    @Override
    public Map<Long, List<RoleDO>> roleListMap(Collection<Long> userCollection) {
        Map<Long, List<RoleDO>> resultMap = new HashMap<>(userCollection.size());
        if (CollUtil.isEmpty(userCollection)) {
            throw new IllegalArgumentException("'userCollection' is empty");
        }
        Map<Long, List<Long>> userRoleMap = userRoleRepository.roleIdListMap(userCollection);
        Set<Long> idList = userRoleMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());

        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery();
        query.eq(RoleDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        query.eq(RoleDO::getStatus, StatusEnum.ENABLE.getCode());
        query.in(RoleDO::getId, idList);
        List<RoleDO> list = baseMapper.selectList(query);
        if (CollUtil.isNotEmpty(list)) {
            Map<Long, RoleDO> map = list.stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
            for (Map.Entry<Long, List<Long>> entry : userRoleMap.entrySet()) {
                List<Long> roleIdList = entry.getValue();
                List<RoleDO> roleList = roleIdList.stream().map(map::get).filter(ObjUtil::isNotEmpty).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(roleList)) {
                    resultMap.put(entry.getKey(), roleList);
                }
            }
        }

        return resultMap;
    }
}
