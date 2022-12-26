# mcdull

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
│    └─mcdull-framework-dependencies            依赖管理
│    ├─mcdull-framework-starters                组件starters  
│ 
├─mcdull-support                                支撑模块（包含技术中台和业务中台）
│    └─mcdull-geteway                           统一网关
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

国际化 测试