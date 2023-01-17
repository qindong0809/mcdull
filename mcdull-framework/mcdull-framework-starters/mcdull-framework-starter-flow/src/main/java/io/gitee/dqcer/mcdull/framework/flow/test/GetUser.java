package io.gitee.dqcer.mcdull.framework.flow.test;

import io.gitee.dqcer.mcdull.framework.flow.node.TreeNode;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Liu
 * @date 2020/5/4 9:16 上午
 */
@TreeNode(code = "user.get.id")
public class GetUser implements ProcessHandler<SimpleContext> {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(SimpleContext context) {
        long id = context.getUserId();
        //模拟从数据库获取user
        User user = new User();
        user.setAge(22);
        user.setId(id);
        user.setName("kkk");
        log.info("查询结果： user:{}",user);
        context.setUser(user);
    }

}
