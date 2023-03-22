package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DeptLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DeptVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDeptService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* 部门 控制器
*
* @author dqcer
* @since 2023-02-08
*/
@RestController
@RequestMapping("/system/dept")
public class DeptController {

    @Resource
    private IDeptService deptService;


    /**
    * 列表查询
    *
    * @param dto dto
    * @return {@link Result<PagedVO>}
    */
    @Authorized("system:dept:list")
    @GetMapping("list")
    public Result<List<DeptVO>> list(@Validated(value = {ValidGroup.List.class}) DeptLiteDTO dto){
        return deptService.list(dto);
    }

    /**
     * 查询部门列表（排除节点）
     *
     * @param deptId 部门id
     * @return {@link Result}<{@link List}<{@link DeptVO}>>
     */
    @GetMapping("list/exclude/{deptId}")
    public Result<List<DeptVO>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId){
        return deptService.excludeChild(deptId);
    }


    /**
     * 查询部门列表（排除节点）
     *
     * @param deptId 部门id
     * @return {@link Result}<{@link List}<{@link DeptVO}>>
     */
    @GetMapping("{deptId}")
    public Result<DeptVO> detail(@PathVariable(value = "deptId", required = false) Long deptId){
        return deptService.detail(deptId);
    }



}
