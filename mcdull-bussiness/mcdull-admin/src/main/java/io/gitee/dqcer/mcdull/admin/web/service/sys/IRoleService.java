package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleVO;
import io.gitee.dqcer.mcdull.framework.base.dto.IdDTO;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

import java.util.List;

/**
* 角色表 业务接口类
*
* @author dqcer
* @since 2023-02-08
*/
public interface IRoleService {


    /**
     * 分页查询
     *
     * @param dto 参数
     * @return {@link Result< PagedVO >}
     */
    Result<PagedVO<RoleVO>> listByPage(RoleLiteDTO dto);

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    Result<Long> insert(RoleLiteDTO dto);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return {@link Result<RoleVO> }
     */
    Result<RoleVO> detail(Long id);

    /**
     * 编辑数据
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    Result<Long> update(RoleLiteDTO dto);
    /**
     * 状态更新
     *
     * @param dto   参数
     * @return {@link Result<Long> }
     */
     Result<Long> updateStatus(StatusDTO dto);

    /**
     * 根据主键集删除
     *
     * @param dto dto
     * @return {@link Result<List>}
     */
    Result<List<Long>> deleteBatchByIds(IdDTO<Long> dto);

    /**
     * 根据主键集查询
     *
     * @param ids id
     * @return {@link Result<List>}
     */
    Result<List<RoleVO>> queryByIds(List<Long> ids);

}
