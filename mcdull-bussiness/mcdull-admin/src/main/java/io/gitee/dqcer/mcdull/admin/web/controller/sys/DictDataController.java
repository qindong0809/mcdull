package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictDataVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDictDataService;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
