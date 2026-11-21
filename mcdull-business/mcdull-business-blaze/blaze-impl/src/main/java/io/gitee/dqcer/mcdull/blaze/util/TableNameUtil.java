package io.gitee.dqcer.mcdull.blaze.util;

import cn.hutool.core.annotation.AnnotationUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;

/**
 * 表名提取工具。
 * <p>
 * 文件远程契约用表名字符串替代 {@code Class<?>} 参数，本工具从业务实体注解提取表名。
 */
public class TableNameUtil {

    private TableNameUtil() {
    }

    public static String tableName(Class<?> clazz) {
        TableName annotation = AnnotationUtil.getAnnotation(clazz, TableName.class);
        if (annotation == null) {
            throw new BusinessException("类上没有TableName注解");
        }
        return annotation.value();
    }
}
