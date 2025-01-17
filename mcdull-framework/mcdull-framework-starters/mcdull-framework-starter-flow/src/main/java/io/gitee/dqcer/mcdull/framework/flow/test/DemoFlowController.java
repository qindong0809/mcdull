package io.gitee.dqcer.mcdull.framework.flow.test;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.flow.ProcessFlow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * 演示流程控制器
 *
 * @author dqcer
 * @since 2023/01/18 22:01:83
 */
@RestController
public class DemoFlowController {

    @Resource
    private ProcessFlow processFlow;

    private final  static  String BIZ_CODE = "process_flow_simple";


    @GetMapping("/")
    public Result flow() {
        SimpleContext context = new SimpleContext();
        context.setUserId(1);
        context.setId(BIZ_CODE);
        processFlow.run(context);
        return Result.success();
    }
}
