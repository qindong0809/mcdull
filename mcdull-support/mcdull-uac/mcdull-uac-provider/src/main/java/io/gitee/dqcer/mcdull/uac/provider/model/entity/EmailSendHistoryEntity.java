package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Data;

/**
 * 邮件发送记录
 *
 * @author dqcer
 * @since  2022/11/16
 */
@TableName("sys_email_send_history")
@Data
public class EmailSendHistoryEntity extends RelEntity<Integer> {

    private String sentTo;

    private String cc;

    private String title;

    private String content;

    private String fileIdArray;
}
