package com.dqcer.mcdull.mdc.client.api;

import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import com.dqcer.mcdull.mdc.client.vo.DictClientVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 码表服务api
 *
 * @author dqcer
 * @version 2022/10/28
 */
public interface DictServiceApi {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result<DictClientVO>}
     */
    @GetMapping(GlobalConstant.INNER_API + "/dict/detail")
    Result<DictClientVO> one(@Validated(value = ValidGroup.One.class) DictClientDTO dto);

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictClientVO}>>
     */
    @GetMapping( GlobalConstant.INNER_API + "/dict/list")
    Result<List<DictClientVO>> list(@Validated(ValidGroup.List.class) DictClientDTO dto);

}
