package io.gitee.dqcer.mcdull.admin.web.controller.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.TicketAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.TicketEditDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.TicketVO;
import io.gitee.dqcer.mcdull.admin.web.service.database.ITicketService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.dto.PkDTO;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
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
    public Result<Long> insert(@RequestBody @Validated(value = {ValidGroup.Insert.class}) TicketAddDTO dto){
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
    @PutMapping("status")
    public Result<Long> updateStatus(@RequestBody @Validated(value = {ValidGroup.Status.class}) StatusDTO dto){
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
