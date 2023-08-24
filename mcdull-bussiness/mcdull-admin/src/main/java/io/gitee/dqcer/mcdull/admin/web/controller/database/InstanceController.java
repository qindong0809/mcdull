package io.gitee.dqcer.mcdull.admin.web.controller.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceListDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GroupVO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.InstanceVO;
import io.gitee.dqcer.mcdull.admin.web.service.database.IGroupService;
import io.gitee.dqcer.mcdull.admin.web.service.database.IInstanceService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.dto.PkDTO;
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
@RequestMapping("/database/instance")
public class InstanceController implements BasicController {

    @Resource
    private IInstanceService instanceService;

    @Resource
    private IGroupService groupService;


    @Transform
    @GetMapping("list")
    public Result<PagedVO<InstanceVO>> list(@Validated InstanceListDTO dto){
        return instanceService.list(dto);
    }

    @GetMapping("base-info-list")
    public Result<List<SelectOptionVO<Long>>> baseInfoListByGroupId(@Validated PkDTO dto){
        return instanceService.baseInfoListByGroupId(dto.getId());
    }

    @GetMapping("back-list")
    public Result<List<SelectOptionVO<Long>>> backList(){
        return instanceService.backList();
    }

    @GetMapping("all-group")
    public Result<List<GroupVO>> allList(){
        return groupService.allList();
    }

    @PostMapping
    public Result<Long> add(@Validated @RequestBody InstanceAddDTO dto){
        return instanceService.add(dto);
    }

    @PutMapping
    public Result<Long> edit(@Validated @RequestBody InstanceEditDTO dto){
        return instanceService.edit(dto);
    }

    @PutMapping("test-connect")
    public Result<Boolean> testConnect(@Validated @RequestBody InstanceAddDTO dto){
        return instanceService.testConnect(dto);
    }

    @DeleteMapping("{id}")
    public Result<Long> remove(@PathVariable(value = "id") Long id){
        return instanceService.remove(id);
    }

    @GetMapping("{id}")
    public Result<InstanceVO> detail(@PathVariable Long id) {
        return instanceService.detail(id);
    }



}
