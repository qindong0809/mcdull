package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.system.facade.model.dto.SerialNumberGenerateDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 流水号生成对外契约（涉及 system 流水号表，远程调用）
 */
public interface SerialNumberApiDef {

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/serial-number/generate")
    List<String> generate(@RequestBody SerialNumberGenerateDTO dto);
}
