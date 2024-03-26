package io.gitee.dqcer.mcdull.mdc.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelDO;
import lombok.Data;

/**
 * 系统邮件模板实体
 *
 * @author dqcer
 * @since  2022/11/16
 */
@TableName("sys_email_template")
@Data
public class SysEmailSendHistoryDO extends RelDO {

    private String sentTo;

    private String cc;

    private String title;

    private String content;

    private String fileIdArray;
}
