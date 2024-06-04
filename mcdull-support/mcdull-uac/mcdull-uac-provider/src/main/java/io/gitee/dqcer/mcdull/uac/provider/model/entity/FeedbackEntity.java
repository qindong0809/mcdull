package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_feedback")
public class FeedbackEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private String code;

    private String feedbackContent;

    private String feedbackAttachment;

    private Integer userId;

}
