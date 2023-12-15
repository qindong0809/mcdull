package io.gitee.dqcer.mcdull.mdc.provider.web.feign;

import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.client.api.DictServiceApi;
import io.gitee.dqcer.mcdull.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictClientVO;
import io.gitee.dqcer.mcdull.mdc.provider.web.service.DictService;
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
public class DictServerFeign implements DictServiceApi {

    @Resource
    private DictService dictService;

    @Override
    public Result<List<DictClientVO>> list(String selectType) {
        return Result.success(dictService.list(selectType));
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link DictClientVO}>
     */
    @Override
    public Result<DictClientVO> one(@Validated(ValidGroup.One.class) DictClientDTO dto) {
        return Result.success();
    }
}
