package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictDataVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDictDataService;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 码表 控制器
 *
 * @author dqcer
 * @since 2023-02-08
 */
@RequestMapping("system/dict/data")
@RestController
public class DictDataController {

    @Resource
    private IDictDataService dictDataService;

    @GetMapping("list")
    public Result<PagedVO<DictDataVO>> list(DictDataLiteDTO dto) {
        return dictDataService.list(dto);
    }

    @PostMapping
    public Result<Long> add(@Validated @RequestBody DictDataAddDTO dto) {
        return dictDataService.add(dto);
    }

    @PutMapping
    public Result<Long> edit(@Validated @RequestBody DictDataEditDTO dto) {
        return dictDataService.edit(dto);
    }

    @DeleteMapping("{id}")
    public Result<Long> remove(@PathVariable(value = "id") Long id) {
        return dictDataService.remove(id);
    }


    /**
     * dict类型
     *
     * @param dictType dict类型
     * @return {@link Result}<{@link List}<{@link DictDataVO}>>
     */
    @GetMapping("type/{dictType}")
    public Result<List<DictDataVO>> dictType(@PathVariable String dictType) {
        return dictDataService.dictType(dictType);
    }

    /**
     * 查询字典数据详细
     *
     * @param dictCode dict类型代码
     * @return {@link Result}<{@link DictDataVO}>
     */
    @GetMapping("{dictCode}")
    public Result<DictDataVO> detail(@PathVariable Long dictCode) {
        return dictDataService.detail(dictCode);
    }
}
