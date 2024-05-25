package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * sys_notice_view_record
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_notice_view_record")
public class NoticeViewRecordEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer noticeId;
    private Integer userId;
    private Integer pageViewCount;
    private String firstIp;
    private String firstUserAgent;
    private String lastIp;
    private String lastUserAgent;
}
