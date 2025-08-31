package io.gitee.dqcer.mcdull.framework.security;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器
 *
 * @author dqcer
 * @since 2023/12/01
 */
public abstract class AbstractWebMvcConfigurer implements WebMvcConfigurer {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${file.storage.local.upload-path:/home/upload/}")
    private String uploadPath;

    protected static final String [] EXCLUDE_PATTERNS = {
            GlobalConstant.LOGIN_URL,
            GlobalConstant.INNER_API + GlobalConstant.ALL_PATTERNS,
            GlobalConstant.FAVICON_ICO,
            GlobalConstant.ACTUATOR_ALL,
            "/druid/**",
            "/upload" + GlobalConstant.ALL_PATTERNS,
            "/doc.html/**",
            "/doc-ui.html/**",
            "/doc-resources/**",
            "/webjars/**",
            "/error",
            "/home/upload" + GlobalConstant.ALL_PATTERNS,
            "/v3/def-docs/**",
            "/v3/api-docs/**",
            "/error",
    };


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
        registry.addResourceHandler("/upload" + GlobalConstant.ALL_PATTERNS).addResourceLocations("file:" + path);
        // 运行访问 static  目录下的资源
        registry.addResourceHandler("/static" + GlobalConstant.ALL_PATTERNS).addResourceLocations("classpath:/static/");
    }

}
