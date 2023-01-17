package io.gitee.dqcer.mcdull.mdc.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictClientVO;
import io.gitee.dqcer.mcdull.mdc.provider.model.dto.DictLiteDTO;
import io.gitee.dqcer.mcdull.mdc.provider.web.service.DictService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * sys dict控制器
 *
 * @author dqcer
 * @version  2022/11/08
 */
@RestController
public class DictController {

    @Resource
    private DictService sysDictService;

    /**
     * 列表分页
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictClientVO}>>
     */
    @GetMapping("/dict/base/list")
    public Result<PagedVO<DictClientVO>> listByPage(@Validated(ValidGroup.Paged.class) DictLiteDTO dto) {
        return sysDictService.listByPage(dto);
    }
}
