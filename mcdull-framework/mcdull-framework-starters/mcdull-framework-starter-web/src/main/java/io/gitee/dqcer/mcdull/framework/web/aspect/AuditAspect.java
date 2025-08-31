package io.gitee.dqcer.mcdull.framework.web.aspect;

import cn.dev33.satoken.annotation.SaCheckEL;
import cn.hutool.core.util.ObjectUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作日志
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Aspect
@Order(GlobalConstant.Order.ASPECT_OPERATION_LOG - 1)
public class AuditAspect {

    private static final Pattern PERM_PATTERN = Pattern.compile("'([^']+)'");

    private String extractPermCode(String spel) {
        Matcher matcher = PERM_PATTERN.matcher(spel);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * 操作日志拦截 ..* 表示任意包或子包
     */
    @Pointcut("execution(* io.gitee..*.controller..*.*(..))")
    public void auditCut() {
        // 切点
    }

    @Before("auditCut()")
    public void around(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(SaCheckEL.class)) {
            SaCheckEL elAnno = method.getAnnotation(SaCheckEL.class);
            if (elAnno != null) {
                String code = extractPermCode(elAnno.value());
                if (code != null) {
                    UnifySession session = UserContextHolder.getSession();
                    if (ObjectUtil.isNotNull(session)) {
                        session.setPermissionCode(code);
                    }
                }
            }
        }
    }

}
