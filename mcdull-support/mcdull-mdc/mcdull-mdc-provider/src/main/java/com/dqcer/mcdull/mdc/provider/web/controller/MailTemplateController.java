package com.dqcer.mcdull.mdc.provider.web.controller;

import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.dto.DictLiteDTO;
import com.dqcer.mcdull.mdc.api.dto.MailTemplateLiteDTO;
import com.dqcer.mcdull.mdc.api.vo.DictVO;
import com.dqcer.mcdull.mdc.api.vo.MailTemplateBaseVO;
import com.dqcer.mcdull.mdc.api.vo.MailTemplateVO;
import com.dqcer.mcdull.mdc.provider.web.service.MailTemplateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 邮箱模板 控制器
 *
 * @author dqcer
 * @version  2022/11/08
 */
@RestController
public class MailTemplateController {

    @Resource
    private MailTemplateService mailTemplateService;

    /**
     * 所有列表
     *
     * @return {@link Result}<{@link List}<{@link MailTemplateVO}>>
     */
    @GetMapping("mail/template/list")
    public Result<List<MailTemplateBaseVO>> listAll() {
        return mailTemplateService.listAll();
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link DictVO}>
     */
    @GetMapping("mail/template/detail")
    public Result<MailTemplateVO> one(@Validated(ValidGroup.One.class) MailTemplateLiteDTO dto) {
        return mailTemplateService.detail(dto);
    }
}