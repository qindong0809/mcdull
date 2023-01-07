package com.dqcer.mcdull.admin.flow.node;

import com.dqcer.framework.base.util.StrUtil;
import org.springframework.core.annotation.AnnotationUtils;

public interface Node<Context> {

    void execute(Context context);

    /**
     * 得到节点的代码
     *
     * @return {@link String}
     */
    default String getNodeCode() {
        Class<? extends Node> aClass = this.getClass();
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
}
