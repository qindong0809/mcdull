
##### 日志级别动态配置
修改nacos的日志级别后，程序实时生效

- 在nacos配置文件管理中新建一个文件（Data ID）默认为log
- 可通过``logging.dataId``修改，配置格式为properties
- 配置内容如下所示
```properties
root=warn
mybatis-plus=warn
com.dqcer=info
com.zaxxer.hikari=warn
com.alibaba.nacos=error

```