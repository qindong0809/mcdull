package io.gitee.dqcer.mcdull.uac.provider.model.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import lombok.Data;

import java.util.Date;

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

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date updateTime;

}
