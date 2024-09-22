package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * sys_message
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_message")
public class MessageEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     *
     * @see io.gitee.dqcer.mcdull.uac.provider.model.enums.MessageTypeEnum
     */
    private Integer messageType;


    /**
     * 接收者id
     */
    private Integer receiverUserId;

    /**
     * 相关业务id
     */
    private String dataId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Boolean readFlag;

    /**
     * 已读时间
     */
    private Date readTime;
}
