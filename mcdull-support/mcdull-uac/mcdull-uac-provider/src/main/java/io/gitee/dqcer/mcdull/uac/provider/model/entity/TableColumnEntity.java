package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * sys_table_column
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_table_column")
public class TableColumnEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer tableId;

    private String columns;
}
