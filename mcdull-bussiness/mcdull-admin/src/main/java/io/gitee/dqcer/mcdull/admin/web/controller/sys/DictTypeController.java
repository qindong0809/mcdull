package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictTypeVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDictTypeService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 码表类型 控制器
 *
 * @author dqcer
 * @since 2023-02-08
 */
@RequestMapping("system/dict/type")
@RestController
@Validated
public class DictTypeController extends BasicController {

    @Resource
    private IDictTypeService dictTypeService;


    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link PagedVO}<{@link DictTypeVO}>>
     */
    @GetMapping("list")
    public Result<PagedVO<DictTypeVO>> list(@Validated(value = {ValidGroup.List.class}) DictTypeLiteDTO dto) {
        return dictTypeService.list(dto);
    }

    @Authorized("system:dict:add")
    @PostMapping
    public Result<Long> add(@RequestBody DictTypeAddDTO dto) {
        return dictTypeService.add(dto);
    }

    @Authorized("system:dict:edit")
    @PutMapping
    public Result<Long> edit(@Validated @RequestBody DictTypeEditDTO dto) {
        return dictTypeService.edit(dto);
    }

    @Authorized("system:dict:remove")
    @DeleteMapping("{id}")
    public Result<Long> remove(@PathVariable(value = "id") Long id) {
        return dictTypeService.remove(id);
    }

    /**
     * 单个详情
     *
     * @param dictId dict id
     * @return {@link Result}<{@link DictTypeVO}>
     */
    @GetMapping("{dictId}")
    public Result<DictTypeVO> detail(@PathVariable Long dictId) {
        return dictTypeService.detail(dictId);
    }

    /**
     * 获取字典选择框列表
     *
     * @return {@link Result}<{@link DictTypeVO}>
     */
    @GetMapping("/option-select")
    public Result<List<DictTypeVO>> getAll() {
        return dictTypeService.getAll();
    }
}
