package io.gitee.dqcer.mcdull.framework.doc;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * Open Api 配置
 * 对JSR303提供支持
 *
 * @author dqcer
 * @since  2023/12/13
 */
@Profile("prod")
@Configuration
@ConditionalOnProperty(name = "doc.enable", havingValue = "true")
public class OpenApiConfig {

    public static final String SAME_TOKEN = "SA-SAME-TOKEN";
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(components())
                .info(new Info()
                        .title("Open API")
                        .contact(new Contact().name("gitee").email("dqcer@sina.com").url("https://mcdull.io"))
                        .version("v1.0")
                        .description("<font color=\"#DC143C\">**以「高质量代码」为核心，「简洁、高效、安全」**</font>快速开发平台。" +
                                "<br/><font color=\"#DC143C\">**《数据安全》**</font>， 支持登录限制。" )
                )
                .addSecurityItem(new SecurityRequirement().addList(SAME_TOKEN));
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes(SAME_TOKEN, new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER).name(SAME_TOKEN));
    }

    @Bean
    public GroupedOpenApi businessApi() {
        return GroupedOpenApi.builder()
                .group("业务接口")
                .pathsToMatch(GlobalConstant.ALL_PATTERNS)
                .addOperationCustomizer(new PermissionOperationCustomizer())
                .build();
    }

}
