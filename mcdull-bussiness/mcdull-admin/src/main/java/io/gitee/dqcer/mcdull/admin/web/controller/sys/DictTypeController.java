package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictTypeVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDictTypeService;
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
 * 码表类型 控制器
 *
 * @author dqcer
 * @since 2023-02-08
 */
@RequestMapping("system/dict/type")
@RestController
public class DictTypeController {

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
