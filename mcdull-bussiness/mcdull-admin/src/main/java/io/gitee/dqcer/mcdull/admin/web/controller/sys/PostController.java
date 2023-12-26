package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.PostLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.PostVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IPostService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* 岗位 控制器
*
* @author dqcer
* @since 2023-02-08
*/
@RestController
@RequestMapping("/system/post")
public class PostController {

    @Resource
    private IPostService postService;


    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Result}<{@link PagedVO}<{@link PostVO}>>
     */
    @Transform
    @Authorized("system:post:list")
    @GetMapping("list")
    public Result<PagedVO<PostVO>> pagedQuery(@Validated(value = {ValidGroup.List.class}) PostLiteDTO dto){
        return postService.pagedQuery(dto);
    }

    /**
     * 明细
     *
     * @param id id
     * @return {@link Result}<{@link PostVO}>
     */
    @Authorized("system:post:query")
    @GetMapping("{id}")
    public Result<PostVO> detail(@PathVariable Long id){
        return postService.detail(id);
    }

    /**
     * 添加
     *
     * @param dto dto
     * @return {@link Result}<{@link Long}>
     */
    @Authorized("system:post:add")
    @PostMapping
    public Result<Long> add(@RequestBody  @Validated(value = {ValidGroup.Insert.class}) PostLiteDTO dto){
        return postService.add(dto);
    }

    @Authorized("system:post:edit")
    @PutMapping
    public Result<Long> update(@RequestBody  @Validated(value = {ValidGroup.Update.class}) PostLiteDTO dto){
        return postService.update(dto);
    }

    @Authorized("system:post:remove")
    @DeleteMapping("{ids}")
    public Result<Long> logicDelete(@PathVariable Long[] ids){
        return postService.logicDelete(ids);
    }
}
