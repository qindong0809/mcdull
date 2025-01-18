package io.gitee.dqcer.blaze.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 订单合同 实体类
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("blaze_order")
public class BlazeOrderEntity extends BaseEntity<Integer> {

    /**
     * 所属人才证书
     */
    private Integer talentCertId;
    /**
     * 所属企业证书
     */
    private Integer customerCertId;
    /**
     * 合同时间
     */
    private Date contractTime;
    /**
     * 备注
     */
    private String remarks;
}