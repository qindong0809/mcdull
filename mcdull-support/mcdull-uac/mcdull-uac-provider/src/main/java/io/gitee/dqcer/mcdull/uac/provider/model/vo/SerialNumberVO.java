package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.Date;

/**
 * @author dqcer
 * @since 2024/05/27
 */
@Data
public class SerialNumberVO implements VO {

    private Integer serialNumberId;

    private String businessName;

    private Integer businessType;

    private String format;

    private String ruleType;

    private Integer initNumber;

    private Integer stepRandomRange;

    private String remark;

    private Integer lastNumber;

    private Date lastTime;
}
