package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * dict key
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_dict_key")
public class DictKeyEntity extends RelEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String keyCode;

    private String keyName;

    private String remark;
}