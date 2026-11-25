package io.gitee.dqcer.mcdull.framework.mysql.aspect;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.SymbolConstants;
import io.gitee.dqcer.mcdull.framework.mysql.annotation.DynamicDataSource;
import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * 动态数据源切面：按 {@link DynamicDataSource} 切换数据源。
 * <p>
 * 栈语义：一次 push 必须对应一次 poll。此前用 {@code @After} + {@code @AfterThrowing} 双通知 +
 * {@code DynamicContextHolder.clear()} 实现，嵌套调用时内层退出会清空整个栈（含外层与请求级默认源），
 * 且异常时两个通知都会触发造成重复清理。现改为单个 {@code @Around} + {@code finally poll()}，
 * 只弹自己压入的那一层。
 * <p>
 * 切点同时匹配方法级（{@code @annotation}）与类级（{@code @within}）注解，与注解的
 * {@code @Target({METHOD, TYPE})} 声明保持一致；类级标注时，类内所有方法都切库。
 *
 * @author dqcer
 * @since 2021/10/09
 */
@Aspect
@Order(GlobalConstant.Order.ASPECT_DATA_SOURCE)
public class DataSourceAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    @Pointcut("@annotation(io.gitee.dqcer.mcdull.framework.mysql.annotation.DynamicDataSource) "
            + "|| @within(io.gitee.dqcer.mcdull.framework.mysql.annotation.DynamicDataSource)")
    public void dataSourcePointCut() {
        // 切点
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 方法级注解优先，未标注时回退到类级
        DynamicDataSource annotation = AnnotatedElementUtils.findMergedAnnotation(method, DynamicDataSource.class);
        if (annotation == null) {
            annotation = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), DynamicDataSource.class);
        }
        if (annotation == null) {
            return joinPoint.proceed();
        }

        String value = calculateValue(annotation.value(), method, joinPoint.getArgs());
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("没有对应的数据源key");
        }
        if (log.isDebugEnabled()) {
            log.debug("切换前数据源：{}，目标：{}", DynamicContextHolder.peek(), value);
        }
        DynamicContextHolder.push(value);
        try {
            return joinPoint.proceed();
        } finally {
            DynamicContextHolder.poll();
            if (log.isDebugEnabled()) {
                log.debug("恢复后数据源：{}", DynamicContextHolder.peek());
            }
        }
    }

    /**
     * spel表达式通过方法入参计算数据源名称
     */
    private String calculateValue(String annoValue, Method method, Object[] arguments) {
        if (!annoValue.startsWith(SymbolConstants.JH)) {
            return annoValue;
        }
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, arguments, NAME_DISCOVERER);
        Object value = PARSER.parseExpression(annoValue).getValue(context);
        return value == null ? null : value.toString();
    }
}
