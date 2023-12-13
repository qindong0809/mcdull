package io.gitee.dqcer.mcdull.framework.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;


/**
 * Open Api 配置
 * 对JSR303提供支持
 *
 * @author dqcer
 * @since  2023/12/13
 */
@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class OpenApiConfig  {
    @Bean
    public OpenAPI springShopOpenApi() {
        return new OpenAPI()
                // 接口文档标题
                .info(new Info().title("McDul Doc")
                        // 接口文档版本
                        .version("v1.0")
                        .contact(new Contact().name("dqcer").url("dqcer@sina.com")))
                .externalDocs(new ExternalDocumentation());
    }
}
