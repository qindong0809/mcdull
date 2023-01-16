package io.gitee.dqcer.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.admin.model.entity.sys.RoleDO;

/**
 * 角色 数据库操作封装接口层
 *
 * @author dqcer
 * @version 2022/12/26
 */
public interface IRoleRepository extends IService<RoleDO> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    Page<RoleDO> selectPage(RoleLiteDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Long insert(RoleDO entity);

}
