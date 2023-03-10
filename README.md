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
* 如果满足您的需求，避免下次迷路，期待您右上角点个 star)


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
│    ├─mcdull-framework-base                    底层定义
│    ├─mcdull-framework-config                  配置定义
│    ├─mcdull-framework-dependencies            依赖管理
│    ├─mcdull-framework-enforcer                框架规则
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

#### 禁止使用bean进行copy
```java
// 反例
BeanUtil.copyProperties(source, target)
```
> 说明：使用反射效率低，其次增加可维护性成本增高，后面接手的小伙伴不知道程序哪些是需要的字段

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


#### 基于用户的协同过滤算法和基于物品的协同过滤算法的对比
- 基于用户的协同过滤算法(user-based collaboratIve filtering 简称 UserCF)
  给用户推荐和他兴趣相似的其他用户喜欢的产品
- 基于物品的协同过滤算法(item-based collaborative filtering 简称 ItemCF)
  给用户推荐和他之前喜欢的物品相似的物品

|   | UserCF  |  ItemCF |
|---|---|---|
| 场景  | 适用于用户较少的场合，如果用户很多，计算用户相似度矩阵代价很大  |  适用于物品数量明显少于用户数的场合，如果物品很多，计算物品相似度矩阵难度很大 |
| 领域 | 时效性较强，用户个性化兴趣不太明显的领域，强调人与人之间的共性(微博热搜)  |  长尾物品(小众物品)丰富,用户个性化需求强烈的领域，强调人的个性 |
| 实时性  | 在用户有新行为，不一定造成推荐结果的立即变化  | 用户有新行为，一定会导致推荐结果的实时变化  |
|  冷启动 | 在新用户对很少的物品产生行为后，不能立即对他进行个性化推荐，因为用户相似度表是每隔一段时间离线计算的（只要用户有新行为，那么相似用户就很有可能发生变化，需要更新相似用户，才能做出准确的推荐）  | 新用户只要对一个物品产生行为，就可以给他推荐和该物品相关的其他物品  |
|  新物品 |  新物品上线一段时间后，一旦有用户对物品产生行为，就可以将新物品推荐给和对它产生行为的用户兴趣相似的其他用户 |   没有办法再不离线更新物品相似度表的情况下将新物品推荐给用户，因为新物品跟其他物品的相似度还没有计算，不能从相似性矩阵中找到对于的相似物品|
|  推荐理由|  很难提供令用户信服的推荐解释 |  利用用户的历史行为给用户做推荐解释，可以令用户比较信服 |