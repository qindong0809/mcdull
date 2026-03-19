package io.gitee.dqcer.mcdull.blaze.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("app_user_focus_time")
public class AppUserFocusTimeEntity extends BaseEntity<Integer> {

/**
 * `app_user_id` int not null comment 'app user id',
 * `duration` int not null comment '专注时长',
 * `focusedTime` datetime not null comment '专注时间',
 * `isCompleted` tinyint(0) not null comment '是否完成',
 * `whiteNoise` varchar(512) not null comment '白噪声',
 * `timestamp` datetime not null comment '时间戳',
 *
 */
    private Integer appUserId;
    private Integer duration;
    private Integer focusedTime;
    private Boolean isCompleted;
    private String whiteNoise;
    private Date timestamp;

}
