package io.gitee.dqcer.mcdull.system.provider.model.vo;


import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * FormVO
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@Data
public class FormItemVO implements VO {

    private Integer id;

    private String name;

    private String key;

}
