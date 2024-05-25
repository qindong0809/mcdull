package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * sys_operate_log
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_operate_log")
public class OperateLogEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String traceId;

    private Integer timeTaken;

    private String module;

    private String content;

    private String url;

    private String method;

    private String param;

    private String ip;

    private String ipRegion;

    private String userAgent;

    private Boolean successFlag;

    private String failReason;

}
