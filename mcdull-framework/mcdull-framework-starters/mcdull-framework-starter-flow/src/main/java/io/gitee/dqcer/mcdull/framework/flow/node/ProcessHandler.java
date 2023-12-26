package io.gitee.dqcer.mcdull.framework.flow.node;

import cn.hutool.core.util.StrUtil;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 流程处理程序
 *
 * @author dqcer
 * @since 2023/01/08 16:01:16
 */
public interface ProcessHandler<Context> {

    /**
     * 执行
     *
     * @param context 上下文
     */
    void execute(Context context);

    /**
     * 得到节点的代码
     *
     * @return {@link String}
     */
    default String getNodeCode() {
        Class<? extends ProcessHandler> aClass = this.getClass();
        TreeNode treeNode = AnnotationUtils.findAnnotation(aClass, TreeNode.class);
        if (treeNode == null) {
            return aClass.getSimpleName();
        }
        String code = treeNode.code();
        if (StrUtil.isNotBlank(code)) {
            return code;
        }
        return aClass.getSimpleName();
    }

    /**
     * 终止该业务整个流程
     *
     * @return boolean
     */
    default boolean stopFlow() {
        return false;
    }

}
