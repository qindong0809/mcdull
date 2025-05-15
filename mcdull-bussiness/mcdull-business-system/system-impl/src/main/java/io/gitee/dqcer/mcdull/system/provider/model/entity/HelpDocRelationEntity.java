package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * sys_help_doc_relation
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_help_doc_relation")
public class HelpDocRelationEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer relationId;

    private String relationName;

    private Integer helpDocId;
}
