package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * sys_serial_number
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_serial_number")
public class SerialNumberEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer businessType;

    private String format;

    private String ruleType;

    private Integer initNumber;

    private Integer stepRandomRange;

    private String remark;

    private Integer lastNumber;

    private Date lastTime;
}