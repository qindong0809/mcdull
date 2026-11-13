package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.TimestampEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件发送记录
 *
 * @author dqcer
 * @since  2022/11/16
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_email_send_history")
@Data
public class EmailSendHistoryEntity extends TimestampEntity<Integer> {

    /**
     * {@link io.gitee.dqcer.mcdull.system.provider.model.enums.EmailTypeEnum}
     */
    private Integer type;

    private String sentTo;

    private String cc;

    private String title;

    private String content;

    private String fileIdArray;
}
