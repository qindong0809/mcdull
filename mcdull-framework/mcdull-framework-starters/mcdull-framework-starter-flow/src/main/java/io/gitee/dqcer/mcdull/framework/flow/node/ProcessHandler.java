package io.gitee.dqcer.mcdull.framework.flow.node;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    default boolean endNode() {
        return false;
    }

    /**
     * 是否继承父节点中的  catchException 和 finallyException 逻辑
     * 即：共用一个  catch  和 finally 逻辑
     *
     * @return boolean
     */
    default boolean extendParentNodeTryAttr(){
        return false;
    }

    default void catchException(Exception exception, Context context){
        Logger log = LoggerFactory.getLogger(this.getClass());
        LogHelp.error(log, "ProcessHandler error. context: {}", context, exception);
    }

    default void finallyException(Exception exception, Context context){
        Logger log = LoggerFactory.getLogger(this.getClass());
        LogHelp.debug(log, "ProcessHandler afterExecute. context: {}", context);
    }

}
