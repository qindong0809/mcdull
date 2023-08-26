package io.gitee.dqcer.mcdull.admin.model.entity.database;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dqcer
 * @since 2022/11/07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("database_back")
public class BackDO extends BaseDO {

    public static final Integer MODEL_TICKET = 1;
    public static final Integer MODEL_GROUP = 2;

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer model;

    private Long bizId;

    private String remark;
}
