package com.dqcer.mcdull.mdc.client.api;

import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import com.dqcer.mcdull.mdc.client.vo.DictClientVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登录服务api
 *
 * @author dqcer
 * @version 2022/10/28
 */
public interface SysDictServiceApi {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result<  DictClientVO  >}
     */
    @GetMapping("/feign/dict/detail")
    Result<DictClientVO> one(@Validated(value = ValidGroup.One.class) DictClientDTO dto);

}
