package com.dqcer.mcdull.framework.web.remote;

import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "mcdull-mdc-provider",  contextId = "commonDict")
public interface DictRemote {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result < EnumVO >}
     */
    @GetMapping(GlobalConstant.FEIGN_PREFIX + "/dict/detail")
    Result<DictVO> detail(@SpringQueryMap @Validated(value = ValidGroup.One.class) DictLiteDTO dto);


    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictVO}>>
     */
    @GetMapping(GlobalConstant.FEIGN_PREFIX + "/dict/list")
    Result<List<DictVO>> list(@SpringQueryMap @Validated(ValidGroup.List.class) DictLiteDTO dto);
}
