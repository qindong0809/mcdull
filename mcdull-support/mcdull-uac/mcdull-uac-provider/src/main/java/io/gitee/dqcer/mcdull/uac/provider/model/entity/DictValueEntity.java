package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * dict value
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_dict_value")
public class DictValueEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer dictKeyId;

    private String valueCode;

    private String valueName;

    private String remark;

    private Integer sort;
}
