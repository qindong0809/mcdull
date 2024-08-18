package io.gitee.dqcer.mcdull.framework.web.config;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.enums.EnvironmentEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * system environment config
 *
 * @author dqcer
 * @since 2023/05/19
 */
@Configuration
public class SystemEnvironmentConfig implements Condition {

    @Value("${spring.profiles.active:dev}")
    private String systemEnvironment;

    @Value("${spring.application.name:unknown}")
    private String projectName;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return isDevOrTest(context);
    }

    private boolean isDevOrTest(ConditionContext conditionContext) {
        String property = conditionContext.getEnvironment().getProperty("spring.profiles.active");
        if (StrUtil.isNotBlank(property)) {
            return !EnvironmentEnum.PROD.getText().equalsIgnoreCase(property);
        }
        return true;
    }

    @Bean("systemEnvironment")
    public SystemEnvironment initEnvironment() {
        EnvironmentEnum environmentEnum = IEnum.getByCode(EnvironmentEnum.class, systemEnvironment);
        if (ObjUtil.isNull(environmentEnum)) {
            throw new IllegalArgumentException("System environment not support");
        }
        SystemEnvironment environment = new SystemEnvironment();
        environment.setProd(EnvironmentEnum.PROD.getCode().equals(environmentEnum.getCode()));
        environment.setProjectName(projectName);
        environment.setEnvironment(environmentEnum.getCode());
        return environment;
    }
}
