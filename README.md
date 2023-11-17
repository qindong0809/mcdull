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

* 在线文档： [点击前往](https://dqcer.gitee.io/mcdull)


#### 介绍
麦兜框架：依赖管理 基类/超类 效验参数 架构规则库 操作稽查组件 缓存组件 多级缓存 多数据源 动态数据源 加解密 MQ组件

### 架构图
![](https://gitee.com/dqcer/mcdull/raw/master/doc/assets/%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

> 画图工具 [excalidraw](https://excalidraw.com/)


### 项目结构 

```
mcdull
├──doc                           文档
│    ├─db                        sql
│    └─yaml                      配置文件
│ 
├─mcdull-bussiness               业务模块
│   
├─mcdull-framework                              基础框架
│    ├─mcdull-framework-agent                     探针模块
│    ├─mcdull-framework-base                      底层定义
│    ├─mcdull-framework-config                    配置定义
│    ├─mcdull-framework-dependencies              依赖管理
│    ├─mcdull-framework-enforcer                  框架规则
│    └─mcdull-framework-starters               组件starters  
│       ├─mcdull-framework-starter-feign          feign组件
│       ├─mcdull-framework-starter-feign          流程编排
│       ├─mcdull-framework-starter-mongodb        mongodb组件
│       ├─mcdull-framework-starter-mysql          mysql组件
│       ├─mcdull-framework-starter-nacos          nacos组件
│       ├─mcdull-framework-starter-oss            对象存储
│       ├─mcdull-framework-starter-redis          redis组件
│       └─mcdull-framework-starter-web            web组件
│ 
├─mcdull-support                              支撑模块（包含技术中台和业务中台）
│    └─mcdull-geteway                             统一网关
│    ├─mcdull-generator                           代码生成器  
│    ├─mcdull-mdc                                 元数据中心  
│    └─mcdull-uac                                 用户中心     

```

### 功能特性

- [x] <img src="https://img.shields.io/badge/-全链路日志追踪-brightgreen.svg" alt="全链路日志追踪">
- [x] 网关动态路由
- [x] 日志打印
  - [x] 基于网关
  - [x] 基于controller
  - [x] 基于数据库表
- [x] RBAC权限控制
- [x] 参数效验
- [x] 枚举封装
- [x] 动态数据源
- [x] 多级缓存
- [x] VO/DO/DTO定义
- [x] 异常拦截
- [x] 码表自动翻译
- [x] 代码生成器
- [x] 分布式锁
- [x] 基于IP的雪花算法
- [x] 基于用户/物品协同的推荐算法
- [x] 支持csv文件解析并支持模糊搜索和指定显示的字段

### 框架优势

1. 模块化架构设计，层次清晰，方便升级。
2. 最小依赖原则，杜绝循环重复依赖。
3. 从真实线上环境中提炼而来。
4. 前沿技术与技术成熟度的平衡选型。
5. 代码洁癖者。
6. 有节制的使用第三方开源组件，最大程度上实现自主可控。
7. 提供了静态检查机制(配合单元测试使用)，避免线上出现不合规范的使用。
8. 四层架构（controller, service, manager, dao） 

### 快速开始

#### 文档部署

- 全局安装``docsify`` ，安装之前必须要安装``nodeJS``
```shell
npm i docsify-cli -g
```
- 进入到文档目录下
```shell
cd doc
```
- 启动文档系统
```shell
docsify serve .
```


### 编码常识

#### 基本类型与包装类型使用标准

关于基本数据类型与包装数据类型的使用标准如下：
- 所有的POJO类属性必须使用包装数据类型。 
- RPC方法的返回值和参数必须使用包装数据类型。 
- 所有的局部变量推荐使用基本数据类型。 
> 说明：POJO类属性没有初值是提醒使用者在需要使用时，必须自己显式地进行赋值，任何NPE问题，或者入库检查，都由使用者来保证。

#### @SuppressWarnings注解
- all	抑制所有警告
- boxing	抑制装箱、拆箱操作时候的警告
- cast	抑制映射相关的警告
- dep-ann	抑制启用注释的警告
- deprecation	抑制过期方法警告
- fallthrough	抑制在 switch 中缺失 breaks 的警告
- finally	抑制 finally 模块没有返回的警告
- hiding	抑制相对于隐藏变量的局部变量的警告
- incomplete-switch	忽略不完整的 switch 语句
- nls	忽略非 nls 格式的字符
- null	忽略对 null 的操作
- rawtypes	使用 generics 时忽略没有指定相应的类型
- restriction	抑制禁止使用劝阻或禁止引用的警告
- serial	忽略在 serializable 类中没有声明 serialVersionUID 变量
- static-access	抑制不正确的静态访问方式警告
- synthetic-access	抑制子类没有按最优方法访问内部类的警告
- unchecked	抑制没有进行类型检查操作的警告
- unqualified-field-access	抑制没有权限访问的域的警告
- unused	抑制没被使用过的代码的警告


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

##### 上传文件 子线程无法获取文件
```java
    private void biz(MultipartFile[] attachmentFiles) {
        this.前置逻辑();
        EmailUtil.sendEmailAsyn(
                () -> {
                    // 模拟复现条件 Thread.sleep(25000);
                    // 可能存在的场景：主线程结束，临时文件被清空，导致子线程业务类无法获取到临时文件而报错(系统找不到指定的文件)。
                    mailApi.sendEmailToMultipleReceiverWithAttachment(attachmentFiles, sendTo, ccs, subject, html(text));
                });

        this.后续逻辑();
    }
```
- 解决方案
> 改成主线程、或者使用file.getInputStream()，以文件流信息存储在内存中


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

##### 使用工具类Arrays.asList()把数组转换成集合时，不能使用其修改集合相关的方法，因为它的add/remove/clear方法会抛出UnsupportedOperationException异常
> asList的返回对象是一个Arrays内部类，并没有实现集合的修改方法。Arrays.asList体现的是适配器模式，只是转换接口，后台的数据仍是数据
```java
        // 反例
        String[] str = new String[]{"1", "2"};
        List<String> list = Arrays.asList(str);
        // 第一种情况运行时异常
        list.add(3);
        // 第二种情况也会随之修改，反之亦然
        str[0] = "1-1";
        System.out.println(list);
        
```

#### 禁止使用bean进行copy
```java
// 反例
BeanUtil.copyProperties(source, target)
// 正例
get/set
```
> 说明：使用反射效率低，其次增加可维护性成本增高，后面接手的小伙伴不知道程序哪些是需要的字段. 
> 使用get/set是最直接，也是最简单的。后续二开/维护排错倍倍香!!!

#### 避免过多冗余、过少的日志

- 打印必要的日志
- 合并打印
- 简化：只打印其id
- 缩写：如write 简化为 w, read 简化为 r
- 压缩：文件压缩处理

> 多说一句： 日志用时方恨少，但切记避免过于冗余


### 常见问题

#### 是否使用swagger
- 结论：尽量避免使用``swagger``
- 原因：代码侵入性强，部分存在兼容问题

#### 是否lombok
- 结论：科学使用``lombok``，不要一上来就使用``@Data``注解
- 原因：有时候bean只需要``get``、``set`` 和 ``toString``,但这哥们给我们生成太多东西了。另外因为是在编译的时候生成的，导致编译速度下降

### 自动化测试组件

> 用于检查 Java 代码的体系结构，命名，层级调用是否满足规约

##### 快速开始

- 导入maven

```xml
        <dependency>
            <groupId>io.gitee.dqcer</groupId>
            <artifactId>mcdull-framework-enforcer</artifactId>
            <scope>test</scope>
        </dependency>
```

- 编码

```java

    private JavaClasses classes;

    @BeforeEach
    public void setUp() {

        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_PACKAGE_INFOS)
                .importPackages("io.gitee");
    }

    @Test
    public void requiredRules() {
        for (ArchRule rule : ArchitectureEnforcer.requiredRules) {
            rule.check(classes);
        }
    }
```

##### 内置规约如下

##### 命名规则
    
1. mapper 层下的类应该以'Mapper'结尾
2. 实现DO的类名应该以'DO'结尾
3. 实现DTO的类名应该以'DTO'结尾
4. 实现VO的类名应该以'VO'结尾
5. 枚举类应该以'Enum'结尾
    

##### 层级调用

1. Mapper数据库访问层仅被Repository数据库包装层调用
2. Repository数据库包装层仅被Service业务层、Manager通用逻辑层调用
3. Manager数据库访问层仅被Service业务层调用
4. Service业务层仅被Controller层和ServerFeign层调用


- 异常处理反例

```java
// 反例
     throw new Exception(); 
     throw new RuntimeException("error"); 
     throw new Throwable("error"); 
class CustomException extends Throwable {
    
}

```

- 控制台打印反例

```java
System.out.println("foo");
System.err.println("bar");

OutputStream out = System.out; 
out.write(bytes)

try {
     // ...
} catch (Exception e) {
     e.printStackTrace(); 
 }
```

### 插件推荐
#### GenerateAllSetter
> 通过alt+enter对变量类生成对类的所有setter方法的调用

#### Free Mybatis plugin
>快速从代码跳转到mapper及从mapper返回代码

#### RestfulTool
>一套 Restful 服务开发辅助工具集

#### Easy Javadoc
>自动生成javadoc文档注释


### 后期规划
- 翻译转换支持批量提高效率
- 文件上传下载
- pdf生成器
- transmittable-thread-local
>动态表单： https://segmentfault.com/q/1010000009146625 
> https://www.jianshu.com/p/b2f4ad0ec396
>log.error("xxxxx", ThrowableUtil.getStackTraceAsString(e));
>如有需求请联系作者(dqcer@sina.com)

### 编码建议

#### 控制流语句“if”、“for”、“while”、“switch”和“try”不应该嵌套得太深
```java
// 反例
if (condition1) {                  // Compliant - depth = 1
  /* ... */
  if (condition2) {                // Compliant - depth = 2
    /* ... */
    for(int i = 0; i < 10; i++) {  // Compliant - depth = 3, not exceeding the limit
      /* ... */
      if (condition4) {            // Noncompliant - depth = 4
        if (condition5) {          // Depth = 5, exceeding the limit, but issues are only reported on depth = 4
          /* ... */
        }
        return;
      }
    }
  }
}
// 正例
if (!condition1) {                 
  /* ... */
    return;
}
if (!condition2) {                
   /* ... */
    return;
}
for(int i = 0; i < 10; i++) {  
 /* ... */
 if (condition4) {            
   if (condition5) {          
     /* ... */
   }
   return;
 }
}
```

#### 禁止导入包import * 和允许import内部类（idea默认是允许导入*的）
- 打开设置，找到 File | Settings | Editor | Code Style | Java 界面的 imports 页签，导入数量设置为999

#### 进行权限的拦截时，优先使用``OncePerRequestFilter``而不是``Filter``
> OncePerRequestFilter 确保其 doFilter() 方法在每个请求中只被调用一次，即使在多个线程并发处理请求的情况下

#### 替换``LoadBalancer``用默认的缓存
> 生产环境中使用Caffeine 缓存以获得更好的性能和内存管理
- 1、``pom``添加依赖
```xml
        <dependency>
            <groupId>com.github.ben-manes.caffeine</groupId>
            <artifactId>caffeine</artifactId>
        </dependency>
```

- 2、``application.yml``添加配置
```yml
spring:
  cache:
    type: caffeine
```

持续更新中... 欢迎点``star``, 避免下次迷路
