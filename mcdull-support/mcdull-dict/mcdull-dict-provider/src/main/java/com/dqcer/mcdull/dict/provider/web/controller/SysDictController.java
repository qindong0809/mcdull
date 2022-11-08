package com.dqcer.mcdull.dict.provider.web.controller;

import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.dict.api.dto.SysDictLiteDTO;
import com.dqcer.mcdull.dict.api.vo.SysDictVO;
import com.dqcer.mcdull.dict.provider.web.service.SysDictService;
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
public class SysDictController {

    @Resource
    private SysDictService sysDictService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link SysDictVO}>>
     */
    @GetMapping("dict/list")
    public Result<List<SysDictVO>> list(@Validated(ValidGroup.List.class) SysDictLiteDTO dto) {
        return sysDictService.list(dto);
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link SysDictVO}>
     */
    @GetMapping("dict/one")
    public Result<SysDictVO> one(@Validated(ValidGroup.One.class) SysDictLiteDTO dto) {
        return sysDictService.one(dto);
    }
}
