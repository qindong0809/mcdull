package io.gitee.dqcer.mcdull.system.provider.model.entity;

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
@TableName("sys_code_generator_config")
public class CodeGeneratorConfigEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private String tableName;
    private String basic;
    private String fields;
    private String insertAndUpdate;
    private String deleteInfo;
    private String queryFields;
    private String tableFields;
    private String detail;

}
