package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * 字典价值视图对象
 * dict value
 *
 * @author dqcer
 * @since 2024/04/28
 */
@Data
public class DictValueVO implements VO {

    private Integer dictValueId;

    private Integer dictKeyId;

    private String valueCode;

    private String valueName;

    private Integer sort;

    private String remark;
}