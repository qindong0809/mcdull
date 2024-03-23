package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单控制器
 *
 * @author dqcer
 * @since 2022/12/26
 */
@RequestMapping("menu")
@RestController
public class MenuController {

//    @Resource
//    private IRoleService roleService;
//
//    /**
//     * 列表
//     *
//     * @param dto dto
//     * @return {@link Result}<{@link List}<{@link UserVO}>>
//     */
//    @GetMapping("base/list")
//    @Transform
//    public Result<PagedVO<RoleVO>> listByPage(@Validated(ValidGroup.Paged.class) RoleLiteDTO dto) {
////        return Result.success(roleService.listByPage(dto));
//        return Result.success();
//    }
//
//    /**
//     * 单个
//     *
//     * @param dto dto
//     * @return {@link Result}<{@link UserVO}>
//     */
//    @GetMapping("base/detail")
//    @Transform
//    public Result<RoleVO> detail(@Validated(ValidGroup.One.class) RoleLiteDTO dto) {
//        return roleService.detail(dto);
//    }
//
//    /**
//     * 新增数据
//     *
//     * @param dto dto
//     * @return {@link Result<Integer> 返回新增主键}
//     */
//    @PostMapping("base/save")
//    public Result<Integer> insert(@RequestBody @Validated(value = {ValidGroup.Insert.class})RoleLiteDTO dto){
//        return roleService.insert(dto);
//    }
//
//    /**
//     * 状态更新
//     *
//     * @param dto dto
//     * @return {@link Result<Integer>}
//     */
//    @PostMapping("base/status")
//    public Result<Integer> updateStatus(@RequestBody @Validated(value = {ValidGroup.Status.class}) RoleLiteDTO dto){
//        return roleService.updateStatus(dto);
//    }
//
//    /**
//     * 单个删除
//     *
//     * @param dto dto
//     * @return {@link Result<Integer>}
//     */
//    @PostMapping("base/delete")
//    public Result<Integer> delete(@RequestBody @Validated(value = {ValidGroup.Delete.class}) UserLiteDTO dto){
//        return roleService.delete(dto);
//    }

}
