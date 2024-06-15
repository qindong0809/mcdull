package io.gitee.dqcer.mcdull.uac.provider.model.vo;


import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * FormVO
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@Data
public class FormVO implements VO {

    private Integer id;

    private String name;

    private String jsonText;

    private Boolean publish;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
