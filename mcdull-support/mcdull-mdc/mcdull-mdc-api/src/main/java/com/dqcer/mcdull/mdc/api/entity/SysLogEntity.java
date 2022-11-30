package com.dqcer.mcdull.mdc.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dqcer.framework.base.entity.SuperId;

import java.util.Date;

@TableName("sys_log")
public class SysLogEntity extends SuperId {

    /**
     * 是否为移动端
     */
    private int mobile;

    /**
     * 操作人的账号主键
     */
    private Long accountId;

    /**
     * 租户主键
     */
    private Long tenantId;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 客户端
     */
    private String browser;

    /**
     * 平台
     */
    private String platform;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 引擎
     */
    private String engine;

    /**
     * 版本
     */
    private String version;

    /**
     * 引擎版本
     */
    private String engineVersion;

    /**
     * 操作时间
     */
    private Date time;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 路径
     */
    private String path;

    /**
     * 耗时
     */
    private Long timeTaken;

    /**
     * http状态
     */
    private Long status;

    /**
     * 参数map
     */
    private String parameterMap;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 请求头
     */
    private String headers;

    /**
     * 响应头
     */
    private String responseBody;
}
