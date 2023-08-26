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
@TableName("database_back_instance")
public class BackInstanceDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long backId;

    private Long instanceId;

    private String hashValue;

    private String fileName;
}
