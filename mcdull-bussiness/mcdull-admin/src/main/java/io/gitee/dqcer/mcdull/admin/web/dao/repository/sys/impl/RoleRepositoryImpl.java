package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleMenuDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.RoleMenuMapper;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.util.StrUtil;
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
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(RoleDO::getName, keyword));
        }
        query.orderByDesc(BaseDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPage(), dto.getPageSize()), query);
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
        entity.setCreatedBy(UserContextHolder.getSession().getUserId());
        entity.setCreatedTime(new Date());
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
        return !baseMapper.selectList(Wrappers.lambdaQuery(roleDO)).isEmpty();
    }

    /**
     * 根据id删除批处理
     *
     * @param ids id
     */
    @Override
    public void deleteBatchByIds(List<Long> ids) {
        int rowSize = baseMapper.deleteBatchIds(ids);
        if (rowSize != ids.size()) {
            log.error("数据插入失败 actual: {}, plan:{}", rowSize, ids.size());
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    /**
     * 获取菜单
     *
     * @param roleId 角色id
     * @return {@link List}<{@link RoleMenuDO}>
     */
    @Override
    public List<RoleMenuDO> getMenuByRole(Long roleId) {
        LambdaQueryWrapper<RoleMenuDO> query = Wrappers.lambdaQuery();
        query.eq(RoleMenuDO::getRoleId, roleId);
        List<RoleMenuDO> list = roleMenuMapper.selectList(query);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }
}
