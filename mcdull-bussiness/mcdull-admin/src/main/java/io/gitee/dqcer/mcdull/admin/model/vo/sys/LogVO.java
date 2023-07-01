package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.admin.framework.transformer.UserTransformer;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

import java.util.Date;

/**
* 日志记录 返回客户端值
*
* @author dqcer
* @since 2023-01-14
*/
@Data
public class LogVO implements VO {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 操作人的账号主键
     */
    private Long accountId;

    @Transform(from = "accountId", transformer = UserTransformer.class)
    private String accountStr;

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
     * 所属系统
     */
    private String model;

    /**
     * 所属菜单
     */
    private String menu;

    /**
     * 所属操作类型
     */
    private String button;

    /**
     * 所属操作 str类型
     */
//    @Transform(from = "type", dataSource = OperationTypeEnum.class)
    private String typeStr;

    private Date createdTime;

    private String log;
}