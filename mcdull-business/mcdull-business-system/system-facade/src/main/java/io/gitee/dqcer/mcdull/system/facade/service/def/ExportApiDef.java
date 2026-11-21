package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ExportContextVO;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 导出上下文对外契约（导出前一次性取回操作人/菜单名/配置，避免多次远程往返）
 */
public interface ExportApiDef {

    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/export/context")
    ExportContextVO context();
}
