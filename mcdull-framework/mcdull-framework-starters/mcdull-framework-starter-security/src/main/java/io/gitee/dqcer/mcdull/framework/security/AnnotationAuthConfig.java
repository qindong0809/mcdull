package io.gitee.dqcer.mcdull.framework.security;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaAnnotationStrategy;
import cn.dev33.satoken.util.SaResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注释身份验证配置
 *
 * @author dqcer
 * @since 2025/08/29
 */
@Configuration
public class AnnotationAuthConfig {

    @Resource
    private MessageSource messageSource;

    @PostConstruct
    public void rewriteSaStrategy() {
        // 重写 SaCheckELRootMap 扩展函数，增加注解鉴权 EL 表达式可使用的根对象
        SaAnnotationStrategy.instance.checkELRootMapExtendFunction = rootMap -> {
            rootMap.put("stpAdmin", StpKit.ADMIN);
        };
    }
}
