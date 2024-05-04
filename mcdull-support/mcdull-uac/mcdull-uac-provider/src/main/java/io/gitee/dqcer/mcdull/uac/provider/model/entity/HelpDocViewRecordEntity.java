package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * sys_help_doc_view_record
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_help_doc_view_record")
public class HelpDocViewRecordEntity extends RelEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Long helpDocId;

    private Long userId;

    private Integer pageViewCount;

}
