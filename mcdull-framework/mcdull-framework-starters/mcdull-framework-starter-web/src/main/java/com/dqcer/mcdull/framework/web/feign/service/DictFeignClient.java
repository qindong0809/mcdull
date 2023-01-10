package com.dqcer.mcdull.framework.web.feign.service;

import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.framework.web.feign.model.DictLiteDTO;
import com.dqcer.mcdull.framework.web.feign.model.DictVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 码表对外调用客户端
 *
 * @author dqcer
 * @version 2022/12/24
 */
@FeignClient(value = "mcdull-mdc-provider",  contextId = "commonDict")
public interface DictFeignClient {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result < EnumVO >}
     */
    @GetMapping(GlobalConstant.INTERIOR_API + "/dict/detail")
    Result<DictVO> detail(@SpringQueryMap @Validated(value = ValidGroup.One.class) DictLiteDTO dto);


    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictVO}>>
     */
    @GetMapping(GlobalConstant.INTERIOR_API + "/dict/list")
    Result<List<DictVO>> list(@SpringQueryMap @Validated(ValidGroup.List.class) DictLiteDTO dto);
}
