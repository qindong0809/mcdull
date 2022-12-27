# mcdull


### 框架介绍
<div align="center"><h3 align="center">mcdull 为企业级环境中提炼出来的cloud微服务版本</h3></div>
<div align="center"><h3 align="center">没有最好的框架，只有最合适的</h3></div>
<p align="center">     
    <p align="center">
        <a href="https://spring.io/projects/spring-cloud">
            <img src="https://img.shields.io/badge/spring--cloud-2021.0.3-orange.svg" alt="spring-cloud">
        </a>
        <a href="http://spring.io/projects/spring-boot">
            <img src="https://img.shields.io/badge/spring--boot-2.7.1-green.svg" alt="spring-boot">
        </a>
        <a href="http://mp.baomidou.com">
            <img src="https://img.shields.io/badge/mybatis--plus-3.5.2-blue.svg" alt="mybatis-plus">
        </a>
        <a href="http://mp.baom1idou.com">
            <img src="https://img.shields.io/badge/spring--cloud--alibaba-2021.0.1.0-lightgrey.svg" alt="mybatis-plus">
        </a>
    </p>
</p>

> 徽章生产网站 https://shields.io/

### 快速链接

* 在线文档： 待补充...
* 如果满足您的需求，很期待您右上角点个 star)
* 

#### 介绍
麦兜框架：依赖管理 基类/超类 效验参数 架构规则库 操作稽查组件 缓存组件 多级缓存 多数据源 动态数据源 加解密 MQ组件

#### 项目结构 

```
mcdull
├──doc                           文档
│    ├─db                        sql
│    └─yaml                      配置文件
│ 
├─mcdull-bussiness               业务模块
│   
├─mcdull-framework                              基础框架
│    ├─mcdull-framework-base                    底层定义
│    ├─mcdull-framework-config                  配置定义
│    ├─mcdull-framework-dependencies            依赖管理
│    └─mcdull-framework-starters                组件starters  
│       └─mcdull-framework-starter-feign        feign组件
│       ├─mcdull-framework-starter-mysql        mysql组件
│       ├─mcdull-framework-starter-nacos        nacos组件
│       ├─mcdull-framework-starter-redis        redis组件
│       ├─mcdull-framework-starter-web          web组件
│ 
├─mcdull-support                                支撑模块（包含技术中台和业务中台）
│    └─mcdull-geteway                           统一网关
│    ├─mcdull-generator                         代码生成器  
│    ├─mcdull-mdc                               元数据中心  
│    └─mcdull-uac                               用户中心  
│       

```



### 编码常识

#### 基本类型与包装类型使用标准

关于基本数据类型与包装数据类型的使用标准如下：
- 所有的POJO类属性必须使用包装数据类型。 
- RPC方法的返回值和参数必须使用包装数据类型。 
- 所有的局部变量推荐使用基本数据类型。 
> 说明：POJO类属性没有初值是提醒使用者在需要使用时，必须自己显式地进行赋值，任何NPE问题，或者入库检查，都由使用者来保证。

### 闭坑指南

##### feign 传参服务端无法接收
- 解决方案
```xml
        <!--feign配置 针对解决：feign调用get请求时，body有参导致服务端自动转换为post请求-->
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-httpclient</artifactId>
        </dependency>
```

##### feign get传对象

- 解决方案
```java
    /**
     * 单个详情（使用 @SpringQueryMap）
     *
     * @param dto dto
     * @return {@link Result < DictVO >}
     */
    @GetMapping("/dict/detail")
    Result<DictVO> detail(@SpringQueryMap @Validated(value = ValidGroup.One.class) DictLiteDTO dto);
```

#### 禁止使用bean进行copy
```java
// 反例
BeanUtil.copyProperties(source, target)
```
> 说明：使用反射效率低，其次增加可维护性成本增高，后面接手的小伙伴不知道程序哪些是需要的字段

### 框架优势

1. 模块化架构设计，层次清晰，方便升级。
2. 最小依赖原则，杜绝循环重复依赖。
3. 从真实线上环境中提炼而来。
4. 前沿技术与技术成熟度的平衡选型。
5. 代码洁癖者。
6. 有节制的使用第三方开源组件，最大程度上实现自主可控。

### 框架功能


### 参与贡献

- 期待您的加入！ 联系作者(dqcer@sina.com)
- 1.  Fork 本仓库
- 2.  新建 Feat_xxx 分支
- 3.  提交代码
- 4.  新建 Pull Request

### 后期规划
```java

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }
```