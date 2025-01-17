package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.NoticeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.NoticeVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.INoticeService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
* 通知公告表 控制器
*
* @author dqcer
* @since 2023-01-19
*/
@RestController
@RequestMapping("/system/notice")
public class NoticeController {

    @Resource
    private INoticeService noticeService;

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Result<PagedVO>}
     */
    @Transform
    @Authorized("system:notice:query")
    @GetMapping("list")
    public Result<PagedVO<NoticeVO>> pagedQuery(@Validated(value = {ValidGroup.Paged.class}) NoticeLiteDTO dto){
        return noticeService.pagedQuery(dto);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id id
     * @return {@link Result<NoticeVO>}
     */
    @Authorized("sys:notice:query")
    @GetMapping("/{id}")
    public Result<NoticeVO> detail(@PathVariable Long id){
        return noticeService.detail(id);
    }

    /**
    * 新增数据
    *
    * @param dto dto
    * @return {@link Result<Long> 返回新增主键}
    */
    @Authorized("sys:notice:add")
    @PostMapping
    public Result<Long> insert(@RequestBody @Validated(value = {ValidGroup.Insert.class}) NoticeLiteDTO dto){
        return noticeService.insert(dto);
    }

    /**
    * 编辑数据
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("sys:notice:update")
    @PutMapping
    public Result<Long> update(@RequestBody @Validated(value = {ValidGroup.Update.class}) NoticeLiteDTO dto){
        return noticeService.update(dto);
    }


    /**
    * 根据主键删除
    *
    * @param ids ids
    * @return {@link Result<Long>}
    */
    @Authorized("sys:notice:remove")
    @DeleteMapping("{ids}")
    public Result<List<Long>> logicDelete(@PathVariable Long[] ids){
        return noticeService.logicDelete(Arrays.asList(ids));
    }
}
