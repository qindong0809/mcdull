package io.gitee.dqcer.mcdull.admin.web.controller.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupListDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GroupVO;
import io.gitee.dqcer.mcdull.admin.web.service.database.IGroupService;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.vo.SelectOptionVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* instance 控制器
*
* @author dqcer
* @since 2023-02-08
*/
@RestController
@RequestMapping("/database/group")
public class GroupController implements BasicController {

    @Resource
    private IGroupService groupService;


    @GetMapping("list")
    public Result<PagedVO<GroupVO>> list(@Validated GroupListDTO dto){
        return groupService.list(dto);
    }

    @GetMapping("base-info-list")
    public Result<List<SelectOptionVO<Long>>> baseInfoList(){
        return groupService.baseInfoList();
    }

    @PostMapping
    public Result<Long> add(@Validated @RequestBody GroupAddDTO dto){
        return groupService.add(dto);
    }

    @PutMapping
    public Result<Long> edit(@Validated @RequestBody GroupEditDTO dto){
        return groupService.edit(dto);
    }

    @DeleteMapping("{id}")
    public Result<Long> remove(@PathVariable(value = "id") Long id){
        return groupService.remove(id);
    }

    @GetMapping("{id}")
    public Result<GroupVO> detail(@PathVariable Long id) {
        return groupService.detail(id);
    }



}
