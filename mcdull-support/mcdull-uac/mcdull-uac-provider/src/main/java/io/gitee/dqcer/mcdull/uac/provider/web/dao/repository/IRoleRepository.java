package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RolePageDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleEntity;

import java.util.List;
import java.util.Map;

/**
 * 角色 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IRoleRepository extends IService<RoleEntity> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleEntity}>
     */
    Page<RoleEntity> selectPage(RolePageDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Integer insert(RoleEntity entity);

    Map<Integer, List<RoleEntity>> roleListMap(Map<Integer, List<Integer>> userRoleMap);

    boolean delete(Integer id, String reason);

    boolean toggleStatus(Integer id, boolean inactive);
}
