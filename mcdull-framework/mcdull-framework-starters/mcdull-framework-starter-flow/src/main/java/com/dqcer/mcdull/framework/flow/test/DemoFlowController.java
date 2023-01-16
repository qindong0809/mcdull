package io.gitee.dqcer.framework.flow.test;

import io.gitee.dqcer.framework.base.wrapper.Result;
import io.gitee.dqcer.framework.flow.ProcessFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoFlowController {

    @Autowired
    private ProcessFlow processFlow;

    private final  static  String BIZ_CODE = "process_flow_simple";


    @GetMapping("/")
    public Result flow() {
        SimpleContext context = new SimpleContext();
        context.setUserId(1);
        context.setId(BIZ_CODE);
        processFlow.run(context);
        return Result.ok();
    }
}
