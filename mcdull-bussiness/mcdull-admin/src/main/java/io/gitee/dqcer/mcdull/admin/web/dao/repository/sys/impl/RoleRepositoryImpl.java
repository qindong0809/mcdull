package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleMenuDO;
import io.gitee.dqcer.mcdull.admin.model.enums.UserTypeEnum;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.RoleMenuMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.entity.MiddleDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.RoleMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 角色 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class RoleRepositoryImpl extends ServiceImpl<RoleMapper, RoleDO> implements IRoleRepository {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Resource
    private RoleMenuMapper roleMenuMapper;


    /**
     * 根据ID列表批量查询数据
     *
     * @param ids id列表
     * @return {@link List}>}
     */
    @Override
    public List<RoleDO> queryListByIds(List<Long> ids) {
        LambdaQueryWrapper<RoleDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(RoleDO::getId, ids);
        wrapper.eq(RoleDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        List<RoleDO> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    @Override
    public Page<RoleDO> selectPage(RoleLiteDTO dto) {
        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery();
        String name = dto.getName();
        if (StrUtil.isNotBlank(name)) {
            query.like(RoleDO::getName, name);
        }
        String status = dto.getStatus();
        if (StrUtil.isNotBlank(status)) {
            query.eq(RoleDO::getStatus, status);
        }
        Date startTime = dto.getStartTime();
        Date endTime = dto.getEndTime();
        if (ObjUtil.isNotNull(startTime) && ObjUtil.isNotNull(endTime)) {
            query.between(MiddleDO::getCreatedTime, startTime, endTime);
        }
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
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
        entity.setStatus(StatusEnum.ENABLE.getCode());
        entity.setCreatedBy(UserContextHolder.currentUserId());
        entity.setCreatedTime(new Date());
        entity.setType(UserTypeEnum.READ_WRITE.getCode());
        int row = baseMapper.insert(entity);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    /**
     * 存在
     *
     * @param roleDO 角色
     * @return boolean
     */
    @Override
    public boolean exist(RoleDO roleDO) {
        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery(roleDO);
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        return !baseMapper.selectList(query).isEmpty();
    }

    /**
     * 根据id删除批处理
     *
     * @param ids id
     */
    @Override
    public void deleteBatchByIds(List<Long> ids) {
        LambdaUpdateWrapper<RoleDO> update = Wrappers.lambdaUpdate();
        update.in(IdDO::getId, ids);
        update.set(BaseDO::getDelFlag, DelFlayEnum.DELETED.getCode());
        update.set(BaseDO::getDelBy, UserContextHolder.currentUserId());
        int rowSize = baseMapper.update(null, update);
        if (rowSize != ids.size()) {
            log.error("数据删除失败 actual: {}, plan:{}", rowSize, ids.size());
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    /**
     * 获取菜单
     *
     * @param roles 角色id集
     * @return {@link List}<{@link RoleMenuDO}>
     */
    @Override
    public List<RoleMenuDO> getMenuByRole(List<Long> roles) {
        LambdaQueryWrapper<RoleMenuDO> query = Wrappers.lambdaQuery();
        query.in(RoleMenuDO::getRoleId, roles);
        List<RoleMenuDO> list = roleMenuMapper.selectList(query);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<RoleDO> getAll() {
        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery();
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        List<RoleDO> list = baseMapper.selectList(query);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }

}
