package io.gitee.dqcer.mcdull.mdc.client.api;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictClientVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SERVICE_MDC + "/dict/detail")
    Result<DictClientVO> one(@Validated(value = ValidGroup.One.class) DictClientDTO dto);

    /**
     * 列表
     *
     * @param selectType selectType
     * @return {@link Result}<{@link List}<{@link DictClientVO}>>
     */
    @GetMapping( GlobalConstant.INNER_API + GlobalConstant.SERVICE_MDC + "/dict/{selectType}/list")
    Result<List<DictClientVO>> list(@PathVariable(name = "selectType") String selectType);

}
