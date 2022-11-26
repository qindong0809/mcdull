package com.dqcer.mcdull.mdc.client.api;

import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.dto.DictLiteDTO;
import com.dqcer.mcdull.mdc.api.vo.DictVO;
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
     * @return {@link Result< DictVO >}
     */
    @GetMapping("/dict/detail")
    Result<DictVO> one(@Validated(value = ValidGroup.One.class) DictLiteDTO dto);

}
