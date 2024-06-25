# 应用场景
1. **```自定义日期显示格式```**
2. **```自定义时区，并显示对应时区的日期```**


# 必要依赖

```xml
<dependency>
    <groupId>io.gitee.dqcer</groupId>
    <artifactId>mcdull-framework-starter-web</artifactId>
</dependency>
```
---

# 场景一： 返回前端
- 示例1: 动态显示日期格式
```java
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;
```
- 示例2：动态显示日期格式和时区转换后的日期
```java
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date updateTime;
```

# 场景二： 接收参数



# 场景三： 导出数据
- 示例1： 使用将日期格式化成字符串
```java
String dateCellValue = CustomSerializer
        .serializeDate(UserContextHolder.getSession(), date, true);
```

# 场景三： 接收参数