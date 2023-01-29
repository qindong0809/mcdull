package io.gitee.dqcer.mcdull.mdc.client.api;

import io.gitee.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictClientVO;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 码表服务api
 *
 * @author dqcer
 * @since 2022/10/28
 */
public interface DictServiceApi {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result< DictClientVO >}
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
