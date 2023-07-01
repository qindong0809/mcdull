package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.MiddleDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户操作日志 实体类
 *
 * @author dqcer
 * @since 2023-01-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_operation_log")
public class LogDO extends MiddleDO {

    private static final long serialVersionUID = 1L;

   /**
    * 操作人的账号主键
    */
    private Long accountId;

   /**
    * 租户主键
    */
    private Long tenantId;

   /**
    * 客户端ip
    */
    private String clientIp;

   /**
    * 用户代理
    */
    private String userAgent;

   /**
    * 请求方法
    */
    private String method;

   /**
    * 路径
    */
    private String path;

   /**
    * 日志跟踪id
    */
    private String traceId;

   /**
    * 耗时
    */
    private Long timeTaken;

   /**
    * 参数map
    */
    private String parameterMap;

   /**
    * 请求头
    */
    private String headers;

    /**
     * 所属操作按钮
     */
    private String button;


    private String log;

}
