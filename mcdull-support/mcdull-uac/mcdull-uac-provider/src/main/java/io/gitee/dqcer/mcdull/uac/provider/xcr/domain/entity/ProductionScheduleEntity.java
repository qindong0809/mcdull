package io.gitee.dqcer.mcdull.uac.provider.xcr.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 生产进度表 实体类
 *
 * @author dqcer
 * @since 2024-08-23 13:55:13
 */

@Data
@TableName("production_schedule")
public class ProductionScheduleEntity extends BaseEntity<Integer> {

    /**
     * 客户
     */
    private String customer;
    /**
     * 联系方式
     */
    private String contactInfo;
    /**
     * 地址
     */
    private String address;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 测量日期
     */
    private Date measurementDate;
    /**
     * 测量人
     */
    private Integer measurementBy;
    /**
     * 制图完成日期
     */
    private Date drawingDate;
    /**
     * 制图人
     */
    private Integer drawingBy;
    /**
     * 客户确认日期
     */
    private Date customerConfirmationDate;
    /**
     * 拆单日期
     */
    private Date orderBreakingDate;
    /**
     * 拆单人
     */
    private Integer orderBreakingBy;
    /**
     * 下生产单日期
     */
    private Date productionOrderDate;
    /**
     * 工序日期
     */
    private Date processDate;
    /**
     * 工序描述
     */
    private String processDescription;
    /**
     * 出货日期
     */
    private Date deliveryDate;
    /**
     * 出货人
     */
    private Integer deliveryBy;
    /**
     * 安装日期
     */
    private Date installationDate;
    /**
     * 安装人
     */
    private Integer installationBy;
    /**
     * 生产周期倒计时
     */
    private Integer productionCycleRemaining;
    /**
     * 备注
     */
    private String remark;
    /**
     * 负责人
     */
    private Integer owner;
}