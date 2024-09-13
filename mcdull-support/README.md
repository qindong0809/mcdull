# 微服务改造说明

1、引入一下模块
```xml
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>mcdull-framework-starter-feign</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
        <groupId>${project.groupId}</groupId>
            <artifactId>mcdull-framework-starter-nacos</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>commons-io</artifactId>
                    <groupId>commons-io</groupId>
                </exclusion>
            </exclusions>
        <version>${project.version}</version>
        </dependency>
        <dependency>
        <groupId>${project.groupId}</groupId>
            <artifactId>mcdull-uac-client</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
        <groupId>${project.groupId}</groupId>
            <artifactId>mcdull-mdc-client</artifactId>
            <version>${project.version}</version>
        </dependency>
```
2、去掉跨域配置
```java

    @Bean
    public CorsFilter corsFilter () {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
```
4、添加feign扫描配置类
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableFeignClientsScan {

    @SuppressWarnings("unused")
    String[] basePackages() default {GlobalConstant.BASE_PACKAGE + ".*.client.service", GlobalConstant.BASE_PACKAGE +  ".framework.web.feign"};
    
}
```
5、添加nacos配置
```yaml
spring:
  cloud:
    nacos:
      server-addr: mcdull.io:8848
      discovery:
        heart-beat-interval: 1
```

6、main类SpringApplication引入以下注解
```java
@EnableDiscoveryClient
@EnableFeignClientsScan
```