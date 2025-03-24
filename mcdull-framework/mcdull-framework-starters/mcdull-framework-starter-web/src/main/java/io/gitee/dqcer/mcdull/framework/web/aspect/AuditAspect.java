package io.gitee.dqcer.mcdull.framework.web.aspect;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjUtil;
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

/**
 * 操作日志
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Aspect
@Order(GlobalConstant.Order.ASPECT_OPERATION_LOG - 1)
public class AuditAspect {

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
        if (method.isAnnotationPresent(SaCheckPermission.class)) {
            SaCheckPermission annotation = method.getAnnotation(SaCheckPermission.class);
            if (null != annotation) {
                String[] value = annotation.value();
                if (value.length > 0) {
                    UnifySession session = UserContextHolder.getSession();
                    if (ObjUtil.isNotNull(session)) {
                        session.setPermissionCode(value[0]);
                    }
                }
            }
        }
    }

}
