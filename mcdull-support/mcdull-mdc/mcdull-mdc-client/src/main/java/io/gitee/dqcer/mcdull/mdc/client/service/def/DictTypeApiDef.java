package io.gitee.dqcer.mcdull.mdc.client.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictTypeClientVO;
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
public interface DictTypeApiDef {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result<  DictTypeClientVO  >}
     */
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SERVICE_MDC + "/dict/detail")
    ResultApi<DictTypeClientVO> one(@Validated(value = ValidGroup.One.class) DictClientDTO dto);

    /**
     * 列表
     *
     * @param selectType selectType
     * @return {@link Result}<{@link List}<{@link DictTypeClientVO}>>
     */
    @GetMapping( GlobalConstant.INNER_API + GlobalConstant.SERVICE_MDC + "/dict/{selectType}/list")
    ResultApi<List<DictTypeClientVO>> list(@PathVariable(name = "selectType") String selectType);

}
