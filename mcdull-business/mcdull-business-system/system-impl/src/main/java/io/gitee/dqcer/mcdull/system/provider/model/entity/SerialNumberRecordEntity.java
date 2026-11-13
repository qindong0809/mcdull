package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * sys_serial_number_record
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_serial_number_record")
public class SerialNumberRecordEntity extends RelEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer serialNumberId;

    private Date recordDate;

    private Integer lastNumber;

    private Date lastTime;

    private Integer count;
}
