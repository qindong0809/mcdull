package io.gitee.dqcer.mcdull.framework.base.help;

import cn.hutool.core.text.CharSequenceUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常帮助
 *
 * @author dqcer
 * @since 2026-11-19
 */
public class ExceptionHelp {

    private ExceptionHelp() {
    }

    /**
     * 生成放入响应体的堆栈字符串。
     * <p>
     * 逻辑自 {@code AbstractExceptionAdvice#buildExceptionStr} 原样搬来，含其既有缺陷：未 flush
     * {@link PrintWriter}，短堆栈不会落到 {@link StringWriter}，因此实际只有超过 1000 字符的堆栈
     * 才会返回截断内容，其余一律空串。搬动期间刻意保持不变，修正它属于安全可见变更，需单独评估。
     *
     * @param exception 异常
     * @return 堆栈字符串
     */
    public static String buildStackStr(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(stringWriter);
        exception.printStackTrace(pw);
        String errorStack = stringWriter.toString();
        if (CharSequenceUtil.isNotBlank(errorStack)) {
            if (errorStack.length() > 1000) {
                return errorStack.substring(0, 1000);
            }
        }
        return CharSequenceUtil.EMPTY;
    }
}
