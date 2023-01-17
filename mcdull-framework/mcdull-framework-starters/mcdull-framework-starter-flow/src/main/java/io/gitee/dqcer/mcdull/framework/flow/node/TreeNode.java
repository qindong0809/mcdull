package io.gitee.dqcer.mcdull.framework.flow.node;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeNode {

    /**
     * 流程代码值唯一标识某一流程 节点名不能重复
     * @return code
     */
    String code() default "";
}
