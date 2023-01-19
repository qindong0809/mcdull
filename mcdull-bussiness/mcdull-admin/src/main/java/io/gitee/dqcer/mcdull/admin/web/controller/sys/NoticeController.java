package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.dto.QueryByIdDTO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.INoticeService;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.NoticeVO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.NoticeLiteDTO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
* 通知公告表 控制器
*
* @author dqcer
* @version 2023-01-19
*/
@RestController
@RequestMapping("/sys-notice")
public class NoticeController {

    @Resource
    private INoticeService noticeService;

    /**
    * 新增数据
    *
    * @param dto dto
    * @return {@link Result<Long> 返回新增主键}
    */
    @Authorized("sys:notice:insert")
    @PostMapping("base/insert")
    public Result<Long> insert(@RequestBody @Validated(value = {ValidGroup.Insert.class}) NoticeLiteDTO dto){
        return noticeService.insert(dto);
    }

    /**
    * 通过主键查询单条数据
    *
    * @param dto dto
    * @return {@link Result<NoticeVO>}
    */
    @GetMapping("base/detail")
    public Result<NoticeVO> detail(@Validated(value = {ValidGroup.Id.class}) QueryByIdDTO dto){
        return noticeService.detail(dto.getId());
    }

    /**
    * 编辑数据
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("sys:notice:update")
    @PutMapping("base/update")
    public Result<Long> update(@RequestBody @Validated(value = {ValidGroup.Update.class}) NoticeLiteDTO dto){
        return noticeService.update(dto);
    }

    /**
    * 状态更新
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("sys:notice:status")
    @PutMapping("base/status")
    public Result<Long> updateStatus(@RequestBody @Validated(value = {ValidGroup.Status.class}) StatusDTO dto){
        return noticeService.updateStatus(dto);
    }

    /**
    * 根据主键删除
    *
    * @param dto dto
    * @return {@link Result<Long>}
    */
    @Authorized("sys:notice:delete")
    @PostMapping("base/delete")
    public Result<Long> delete(@RequestBody @Validated(value = {ValidGroup.Delete.class}) NoticeLiteDTO dto){
        return noticeService.delete(dto);
    }

    /**
    * 分页查询
    *
    * @param dto dto
    * @return {@link Result<PagedVO>}
    */
    @Authorized("sys:notice:view")
    @GetMapping("base/list")
    public Result<PagedVO<NoticeVO>> listByPage(@Validated(value = {ValidGroup.Paged.class}) NoticeLiteDTO dto){
        return noticeService.listByPage(dto);
    }

}
