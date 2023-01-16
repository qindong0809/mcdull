package io.gitee.dqcer.mdc.provider.web.feign;

import io.gitee.dqcer.framework.base.validator.ValidGroup;
import io.gitee.dqcer.framework.base.wrapper.Result;
import io.gitee.dqcer.mdc.client.api.DictServiceApi;
import io.gitee.dqcer.mdc.client.dto.DictClientDTO;
import io.gitee.dqcer.mdc.client.vo.DictClientVO;
import io.gitee.dqcer.mdc.provider.web.service.DictService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 码表 feign 实现层
 *
 * @author dqcer
 * @version 2022/12/25
 */
@RestController
public class DictServerFeign implements DictServiceApi {

    @Resource
    private DictService sysDictService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictClientVO}>>
     */
    @Override
    public Result<List<DictClientVO>> list(@Validated(ValidGroup.List.class) DictClientDTO dto) {
        return sysDictService.list(dto);
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link DictClientVO}>
     */
    @Override
    public Result<DictClientVO> one(@Validated(ValidGroup.One.class) DictClientDTO dto) {
        return sysDictService.one(dto);
    }
}
