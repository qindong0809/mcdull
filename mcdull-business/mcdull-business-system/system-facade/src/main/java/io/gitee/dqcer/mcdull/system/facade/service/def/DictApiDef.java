package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.system.facade.model.enums.DictSelectTypeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 字典翻译对外契约（涉及 system 字典表，远程调用）
 */
public interface DictApiDef {

    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/dict/translate")
    KeyValueBO<String, String> translate(@RequestParam("selectType") DictSelectTypeEnum selectType,
                                         @RequestParam("value") String value);
}
