package io.gitee.dqcer.mcdull.framework.swagger;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限code 解析
 *
 * @author dqcer
 * @since 2024/04/25
 */

public class PermissionOperationCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {

        // 权限
        List<String> noteList = new ArrayList<>(getPermission(handlerMethod));
        // 更新
        operation.setDescription(StrUtil.join("<br/>", noteList));

        return operation;
    }


    private List<String> getPermission(HandlerMethod handlerMethod) {
        List<String> values = new ArrayList<>();

        StringBuilder permissionStringBuilder = new StringBuilder();
        SaCheckPermission classPermissions = handlerMethod.getBeanType().getAnnotation(SaCheckPermission.class);
        if (classPermissions != null) {
            permissionStringBuilder.append("<font style=\"color:red\" class=\"light-red\">");
            permissionStringBuilder.append("类：").append(getAnnotationNote(classPermissions.value(), classPermissions.mode()));
            permissionStringBuilder.append("</font></br>");
        }

        SaCheckPermission methodPermission = handlerMethod.getMethodAnnotation(SaCheckPermission.class);
        if (methodPermission != null) {
            permissionStringBuilder.append("<font style=\"color:red\" class=\"light-red\">");
            permissionStringBuilder.append("方法：").append(getAnnotationNote(methodPermission.value(), methodPermission.mode()));
            permissionStringBuilder.append("</font></br>");
        }

        if (permissionStringBuilder.length() > 0) {
            permissionStringBuilder.insert(0, "<font style=\"color:red\" class=\"light-red\">权限校验：</font></br>");
            values.add(permissionStringBuilder.toString());
        }


        StringBuilder roleStringBuilder = new StringBuilder();
        SaCheckRole classCheckRole = handlerMethod.getBeanType().getAnnotation(SaCheckRole.class);
        if (classCheckRole != null) {
            roleStringBuilder.append("<font style=\"color:red\" class=\"light-red\">");
            roleStringBuilder.append("类：").append(getAnnotationNote(classCheckRole.value(), classCheckRole.mode()));
            roleStringBuilder.append("</font></br>");
        }

        SaCheckRole methodCheckRole = handlerMethod.getMethodAnnotation(SaCheckRole.class);
        if (methodCheckRole != null) {
            roleStringBuilder.append("<font style=\"color:red\" class=\"light-red\">");
            roleStringBuilder.append("方法：").append(getAnnotationNote(methodCheckRole.value(), methodCheckRole.mode()));
            roleStringBuilder.append("</font></br>");
        }

        if (roleStringBuilder.length() > 0) {
            roleStringBuilder.insert(0, "<font style=\"color:red\" class=\"light-red\">角色校验：</font></br>");
            values.add(roleStringBuilder.toString());
        }

        return values;
    }

    private String getAnnotationNote(String[] values, SaMode mode) {
        if (mode.equals(SaMode.AND)) {
            return String.join(" and ", values);
        } else {
            return String.join(" or ", values);
        }
    }
}
