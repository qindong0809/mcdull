package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dqcer
 * @since 2022/12/26
 */
@Tag(name = "Dept API")
@RequestMapping("dept")
@RestController
public class DeptController {

    @Resource
    private IDepartmentService deptService;


    @GetMapping("list")
    public Result<List<DepartmentVO>> list(@Validated DeptListDTO dto) {
        return Result.success(deptService.list(dto));
    }

    @GetMapping("list-all")
    public Result<List<DepartmentVO>> getAll() {
        return Result.success(deptService.getAll());
    }


    @PostMapping("insert")
    public Result<Boolean> insert(@RequestBody @Validated DeptInsertDTO dto){
        return Result.success(deptService.insert(dto));
    }

    @PutMapping("{id}/update")
    public Result<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated DeptUpdateDTO dto){
        return Result.success(deptService.update(id, dto));
    }

    @DeleteMapping("{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id, @Validated ReasonDTO dto){
        return Result.success(deptService.delete(id, dto));
    }



}
