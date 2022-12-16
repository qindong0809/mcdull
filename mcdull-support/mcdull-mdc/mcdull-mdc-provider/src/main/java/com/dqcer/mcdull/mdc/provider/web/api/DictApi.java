package com.dqcer.mcdull.mdc.provider.web.api;

import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.dto.DictFeignDTO;
import com.dqcer.mcdull.mdc.api.vo.DictVO;
import com.dqcer.mcdull.mdc.provider.web.service.DictService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DictApi {

    @Resource
    private DictService sysDictService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictVO}>>
     */
    @GetMapping(GlobalConstant.FEIGN_PREFIX + "/dict/list")
    public Result<List<DictVO>> list(@Validated(ValidGroup.List.class) DictFeignDTO dto) {
        return sysDictService.list(dto);
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link DictVO}>
     */
    @GetMapping({ GlobalConstant.FEIGN_PREFIX + "/dict/detail"})
    public Result<DictVO> one(@Validated(ValidGroup.One.class) DictFeignDTO dto) {
        return sysDictService.one(dto);
    }
}
