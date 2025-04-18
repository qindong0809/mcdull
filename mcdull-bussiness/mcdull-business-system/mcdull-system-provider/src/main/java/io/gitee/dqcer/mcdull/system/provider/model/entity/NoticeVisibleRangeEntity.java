package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * sys_notice_visible_range
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_notice_visible_range")
public class NoticeVisibleRangeEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer noticeId;

    private Integer dataType;

    private Integer dataId;

}
