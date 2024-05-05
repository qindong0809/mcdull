package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

/**
 * sys_change_log
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_change_log")
public class ChangeLogEntity extends RelEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String version;

    private Integer type;

    private String publishAuthor;

    private LocalDate publicDate;

    private String content;

    private String link;
}
