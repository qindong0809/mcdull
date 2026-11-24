package io.gitee.dqcer.mcdull.framework.doc;

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jAutoConfiguration;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jSetting;
import com.github.xiaoymin.knife4j.spring.extension.Knife4jOpenApiCustomizer;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Knife4j 4.5.0 自带的 Knife4jOpenApiCustomizer 按 SpringDoc 2.3 的
 * getGroupConfigs(): List 签名编译，升级到 SpringDoc 2.8 后会抛 NoSuchMethodError。
 * 这里注册兼容实现，保留 x-setting / x-markdownFiles 扩展。
 *
 * @author dqcer
 */
@AutoConfiguration
@AutoConfigureBefore(Knife4jAutoConfiguration.class)
@ConditionalOnProperty(name = "knife4j.enable", havingValue = "true")
public class Knife4jCompatConfiguration {

    @Bean
    @ConditionalOnMissingBean(Knife4jOpenApiCustomizer.class)
    public Knife4jOpenApiCustomizer knife4jOpenApiCustomizer(Knife4jProperties knife4jProperties,
                                                             SpringDocConfigProperties docProperties) {
        return new CompatibleCustomizer(knife4jProperties, docProperties);
    }

    static class CompatibleCustomizer extends Knife4jOpenApiCustomizer {

        private final Knife4jProperties knife4jProperties;

        CompatibleCustomizer(Knife4jProperties knife4jProperties, SpringDocConfigProperties docProperties) {
            super(knife4jProperties, docProperties);
            this.knife4jProperties = knife4jProperties;
        }

        @Override
        public void customise(OpenAPI openApi) {
            if (!knife4jProperties.isEnable()) {
                return;
            }
            Knife4jSetting setting = knife4jProperties.getSetting();
            OpenApiExtensionResolver resolver =
                    new OpenApiExtensionResolver(setting, knife4jProperties.getDocuments());
            resolver.start();
            Map<String, Object> extensions = new HashMap<>();
            extensions.put(GlobalConstants.EXTENSION_OPEN_SETTING_NAME, setting);
            extensions.put(GlobalConstants.EXTENSION_OPEN_MARKDOWN_NAME, resolver.getMarkdownFiles());
            openApi.addExtension(GlobalConstants.EXTENSION_OPEN_API_NAME, extensions);
        }
    }
}
