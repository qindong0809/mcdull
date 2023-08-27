package io.gitee.dqcer.mcdull.admin.web.controller.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.*;
import io.gitee.dqcer.mcdull.admin.model.vo.database.BackListVO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.BackVO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.TicketVO;
import io.gitee.dqcer.mcdull.admin.web.service.database.ITicketService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.dto.PkDTO;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
*  控制器
*
* @author dqcer
* @since 2023-08-17
*/
@RestController
@RequestMapping("/database-ticket")
public class TicketController {

    @Resource
    private ITicketService ticketService;

    /**
    * 新增数据
    *
    * @param dto dto
    * @return {@link Result<Long> 返回新增主键}
    */
    @Authorized("database:ticket:insert")
    @PostMapping("insert")
    public Result<Long> insert(@RequestBody @Validated TicketAddDTO dto){
        return ticketService.insert(dto);
    }

    /**
    * 通过主键查询单条数据
    *
    * @param dto dto
    * @return {@link Result<TicketVO>}
    */
    @GetMapping("detail")
    public Result<TicketVO> detail(@Validated PkDTO dto){
        return ticketService.detail(dto.getId());
    }

    /**
    * 编辑数据
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("database:ticket:update")
    @PutMapping("update")
    public Result<Long> update(@RequestBody @Validated TicketEditDTO dto){
        return ticketService.update(dto);
    }
    /**
    * 状态更新
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("database:ticket:status")
    @PostMapping("status")
    public Result<Long> updateStatus(@RequestBody @Validated TicketFollowStatusDTO dto){
        return ticketService.updateStatus(dto);
    }

    /**
    * 根据主键删除
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("database:ticket:delete")
    @PostMapping("delete")
    public Result<Long> deleteById(@RequestBody @Valid PkDTO dto){
        return ticketService.deleteById(dto.getId());
    }

    @GetMapping("instance-back-list")
    public Result<PagedVO<BackListVO>> backByTicketList(@Validated BackListDTO dto){
        return ticketService.backByTicketList(dto);
    }

    @PostMapping("back/add")
    public Result<Long> insertBack(@RequestBody @Validated BackAddDTO dto){
        return ticketService.insertBack(dto);
    }

    @PutMapping("back/update")
    public Result<Long> updateBack(@RequestBody @Validated BackEditDTO dto){
        return ticketService.updateBack(dto);
    }

    @PostMapping("back/delete")
    public Result<Long> deleteByIdBack(@RequestBody @Valid PkDTO dto){
        return ticketService.deleteByIdBack(dto.getId());
    }



    @GetMapping("back/detail")
    public Result<BackVO> backDetail(@Validated PkDTO dto){
        return ticketService.backDetail(dto.getId());
    }

    @PostMapping("back/rollback")
    public Result<Boolean> rollbackByTicket(@RequestBody @Valid PkDTO dto){
        return ticketService.rollbackByTicket(dto.getId());
    }

    @PostMapping("run-script")
    public Result<Boolean> runScript(@RequestBody @Valid PkDTO dto){
        return ticketService.runScript(dto.getId());
    }


    /**
    * 分页查询
    *
    * @param dto dto
    * @return {@link Result<PagedVO>}
    */
    @Authorized("database:ticket:view")
    @GetMapping("list")
    public Result<PagedVO<TicketVO>> listByPage(@Validated(value = {ValidGroup.Paged.class}) TicketAddDTO dto){
        return ticketService.listByPage(dto);
    }

    @PostMapping("sql-script/execute")
    public Result<Long> executeSqlScript(@RequestBody @Valid PkDTO dto){
        return ticketService.executeSqlScript(dto.getId());
    }
}
