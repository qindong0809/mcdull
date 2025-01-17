package io.gitee.dqcer.mcdull.admin.web.controller.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.GitAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GitEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GitListDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GitVO;
import io.gitee.dqcer.mcdull.admin.web.service.database.IGitService;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
* instance 控制器
*
* @author dqcer
* @since 2023-02-08
*/
@RestController
@RequestMapping("/database/git")
public class GitController extends BasicController {

    @Resource
    private IGitService GitService;


    @GetMapping("list")
    public Result<PagedVO<GitVO>> list(@Validated GitListDTO dto){
        return GitService.list(dto);
    }

    @PostMapping
    public Result<Long> add(@Validated @RequestBody GitAddDTO dto){
        return GitService.add(dto);
    }

    @PutMapping
    public Result<Long> edit(@Validated @RequestBody GitEditDTO dto){
        return GitService.edit(dto);
    }

    @DeleteMapping("{id}")
    public Result<Long> remove(@PathVariable(value = "id") Long id){
        return GitService.remove(id);
    }

    @GetMapping("{id}")
    public Result<GitVO> detail(@PathVariable Long id) {
        return GitService.detail(id);
    }



}
