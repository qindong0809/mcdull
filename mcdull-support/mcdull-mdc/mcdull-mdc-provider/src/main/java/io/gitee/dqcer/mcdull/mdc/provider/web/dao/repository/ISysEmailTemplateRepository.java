package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.SysEmailTemplateDO;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface ISysEmailTemplateRepository extends IService<SysEmailTemplateDO> {


    /**
     * map
     *
     * @param codeList codeList
     * @return {@link Map}<{@link String}, {@link SysEmailTemplateDO}>
     */
    Map<String, SysEmailTemplateDO> map(List<String> codeList);
}
