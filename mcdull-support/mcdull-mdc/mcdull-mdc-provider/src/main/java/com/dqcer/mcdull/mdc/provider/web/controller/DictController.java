package com.dqcer.mcdull.mdc.provider.web.controller;

import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.dto.DictFeignDTO;
import com.dqcer.mcdull.mdc.api.dto.DictLiteDTO;
import com.dqcer.mcdull.mdc.api.vo.DictVO;
import com.dqcer.mcdull.mdc.provider.web.service.DictService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * sys dict控制器
 *
 * @author dqcer
 * @version  2022/11/08
 */
@RestController
public class DictController {

    @Resource
    private DictService sysDictService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictVO}>>
     */
    @GetMapping("feign/dict/list")
    public Result<List<DictVO>> list(@Validated(ValidGroup.List.class) DictFeignDTO dto) {
        return sysDictService.list(dto);
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link DictVO}>
     */
    @GetMapping({ "feign/dict/detail"})
    public Result<DictVO> one(@Validated(ValidGroup.One.class) DictFeignDTO dto) {
        return sysDictService.one(dto);
    }

    /**
     * 列表分页
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictVO}>>
     */
    @GetMapping("/dict/base/list")
    public Result<PagedVO<DictVO>> listByPage(@Validated(ValidGroup.Paged.class) DictLiteDTO dto) {
        return sysDictService.listByPage(dto);
    }
}
