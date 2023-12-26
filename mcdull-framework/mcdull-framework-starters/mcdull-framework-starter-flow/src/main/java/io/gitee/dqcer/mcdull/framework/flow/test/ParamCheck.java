package io.gitee.dqcer.mcdull.framework.flow.test;


import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.node.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参数检查
 *
 * @author dqcer
 * @since 2023/01/30 22:01:74
 */

@TreeNode(code = "user.param.check")
public class ParamCheck implements ProcessHandler<SimpleContext> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(SimpleContext context) {
        long userId = context.getUserId();
        log.info("参数校验开始： userId:{}",userId);
        if(userId <= 0) {
            throw new IllegalArgumentException("参数校验失败");
        }
    }

}
