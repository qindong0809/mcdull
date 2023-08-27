package io.gitee.dqcer.mcdull.admin.web.service.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.*;
import io.gitee.dqcer.mcdull.admin.model.vo.database.BackListVO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.BackVO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.TicketVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

import java.util.List;

/**
*  业务接口类
*
* @author dqcer
* @since 2023-08-17
*/
public interface ITicketService {

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    Result<Long> insert(TicketAddDTO dto);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return {@link Result<TicketVO> }
     */
    Result<TicketVO> detail(Long id);

    /**
     * 编辑数据
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    Result<Long> update(TicketEditDTO dto);
    /**
     * 状态更新
     *
     * @param dto   参数
     * @return {@link Result<Long> }
     */
     Result<Long> updateStatus(TicketFollowStatusDTO dto);

    /**
     * 根据主键批量删除
     *
     * @param id id
     * @return {@link Result<Long>}
     */
    Result<Long> deleteById(Long id);


    /**
     * 根据主键集查询
     *
     * @param ids id
     * @return {@link Result<List>}
     */
    Result<List<TicketVO>> queryByIds(List<Long> ids);

    /**
     * 分页查询
     *
     * @param dto 参数
     * @return {@link Result<PagedVO>}
     */
    Result<PagedVO<TicketVO>> listByPage(TicketAddDTO dto);

    Result<Long> executeSqlScript(Long id);

    Result<Boolean> rollbackByTicket(Long id);

    Result<Boolean> runScript(Long id);

    Result<PagedVO<BackListVO>> backByTicketList(BackListDTO dto);

    Result<Long> insertBack(BackAddDTO dto);

    Result<BackVO> backDetail(Long backId);

    Result<Long> updateBack(BackEditDTO dto);

    Result<Long> deleteByIdBack(Long id);
}
