package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.EmailTemplateDO;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IEmailTemplateRepository extends IService<EmailTemplateDO> {


    /**
     * map
     *
     * @param codeList codeList
     * @return {@link Map}<{@link String}, {@link EmailTemplateDO}>
     */
    Map<String, EmailTemplateDO> map(List<String> codeList);
}
