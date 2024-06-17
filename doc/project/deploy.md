# 部署教程

## 环境准备

1. 必要环境``java1.8``、``Idea``、``Maven``、``Redis``、``MySQL5.7+或8.0.14+``、``nodejs``
2. 拉取后端工程[mcdull](https://gitee.com/dqcer/mcdull.git)
3. 初始化数据库脚本
   - ./sql/3.0.sql
   - ./sql/sys_area.sql
4. 准备好redis
5. 修改配置文件
    - ./mcdull-support/mcdull-uac/mcdull-uac-provider/src/main/resources/application-dev.yml
```properties
# 数据库配置
# redis配置
  
```

## 必要启动模块

1. 启动基础服务 ``mcdull-uac-provider`` 模块中的``UserDataContentApplication``中的``main``方法·
> 启动前需要配置对于的环境变量 

```shell
spring.profiles.active=dev
```

2. 启动前端工程[mcdull-uac-web](https://gitee.com/dqcer/mcdull-vue3.git)
> 具体运行方式请参考 mcdull-vue3/README.md

## 扩展模块
- 略

## 打包工程
- 略



