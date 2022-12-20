package com.dqcer.mcdull.mdc.provider.web.feign;

import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.client.api.DictServiceApi;
import com.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import com.dqcer.mcdull.mdc.client.vo.DictClientVO;
import com.dqcer.mcdull.mdc.provider.web.service.DictService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DictServerFeign implements DictServiceApi {

    @Resource
    private DictService sysDictService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictClientVO}>>
     */
    @Override
    public Result<List<DictClientVO>> list(@Validated(ValidGroup.List.class) DictClientDTO dto) {
        return sysDictService.list(dto);
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link DictClientVO}>
     */
    @Override
    public Result<DictClientVO> one(@Validated(ValidGroup.One.class) DictClientDTO dto) {
        return sysDictService.one(dto);
    }
}
