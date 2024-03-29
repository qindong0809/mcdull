package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleInsertDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleUpdateDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleVO;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

/**
* 角色表 业务接口类
*
* @author dqcer
* @since 2023-02-08
*/
public interface IRoleService {

    Result<PagedVO<RoleVO>> listByPage(RoleLiteDTO dto);


    Result<Long> insert(RoleInsertDTO dto);

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
    Result<Long> update(RoleUpdateDTO dto);
    /**
     * 状态更新
     *
     * @param dto   参数
     * @return {@link Result<Long> }
     */
     Result<Long> updateStatus(StatusDTO dto);



    Result<Long> deleteById(Long roleId);
}
