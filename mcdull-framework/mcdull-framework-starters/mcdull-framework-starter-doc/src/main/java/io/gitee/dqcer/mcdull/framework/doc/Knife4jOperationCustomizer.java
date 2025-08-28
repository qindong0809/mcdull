package io.gitee.dqcer.mcdull.framework.doc;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

/**
 * @author dqcer
 * @since 2025/08/28
 */
@Component
public class Knife4jOperationCustomizer implements GlobalOperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        List<SecurityRequirement> security = operation.getSecurity();
        if (security == null) {
            security = List.of(new SecurityRequirement().addList(OpenApiConfig.SAME_TOKEN));
            operation.setSecurity(security);
        }
        return operation;
    }
}
