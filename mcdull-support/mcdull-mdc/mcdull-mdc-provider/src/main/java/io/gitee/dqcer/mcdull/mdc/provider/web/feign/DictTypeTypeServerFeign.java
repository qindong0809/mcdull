package io.gitee.dqcer.mcdull.mdc.provider.web.feign;

import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mcdull.mdc.client.service.def.DictTypeApiDef;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictTypeClientVO;
import io.gitee.dqcer.mcdull.mdc.provider.web.service.IDictTypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 码表 feign 实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@RestController
public class DictTypeTypeServerFeign implements DictTypeApiDef {

    @Resource
    private IDictTypeService dictTypeService;

    @Override
    public ResultApi<List<DictTypeClientVO>> list(String selectType) {
        return ResultApi.success(dictTypeService.list(selectType));
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link DictTypeClientVO}>
     */
    @Override
    public ResultApi<DictTypeClientVO> one(@Validated(ValidGroup.One.class) DictClientDTO dto) {
        return ResultApi.success();
    }
}
