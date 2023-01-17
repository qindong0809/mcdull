/*
 * Copyright (c) 2018. paascloud.net All Rights Reserved.
 * 项目名称：paascloud快速搭建企业级分布式微服务平台
 * 类名称：PaascloudCoreConfig.java
 * 创建人：刘兆明
 * 联系方式：paascloud.net@gmail.com
 * 开源地址: https://github.com/paascloud
 * 博客地址: http://blog.paascloud.net
 * 项目官网: http://paascloud.net
 */
package io.gitee.dqcer.mcdull.framework.config;


import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * mcdull 配置
 *
 * @author dqcer
 * @date 2022/12/18
 */
@Configuration
@EnableConfigurationProperties(McdullProperties.class)
public class McdullConfig {
}
