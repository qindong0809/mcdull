package com.dqcer.mcdull.dict.client.api;

import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.dict.api.dto.SysDictLiteDTO;
import com.dqcer.mcdull.dict.api.vo.SysDictVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登录服务api
 *
 * @author dongqin
 * @date 2022/10/28
 */
public interface SysDictServiceApi {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result<SysDictVO>}
     */
    @GetMapping("sys/dice/detail")
    Result<SysDictVO> one(@Validated(value = ValidGroup.One.class) SysDictLiteDTO dto);

}
