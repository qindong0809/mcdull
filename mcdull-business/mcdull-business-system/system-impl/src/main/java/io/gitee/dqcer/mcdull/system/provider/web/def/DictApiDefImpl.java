package io.gitee.dqcer.mcdull.system.provider.web.def;

import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.system.facade.model.enums.DictSelectTypeEnum;
import io.gitee.dqcer.mcdull.system.facade.service.DictApi;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDictValueService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典翻译实现：同时作为本地 {@code @Primary} bean 与 HTTP 服务端点。
 */
@Primary
@RestController
public class DictApiDefImpl implements DictApi {

    @Resource
    private IDictValueService dictValueService;

    @Override
    public KeyValueBO<String, String> translate(DictSelectTypeEnum selectType, String value) {
        return dictValueService.dictVO(selectType, value);
    }
}
