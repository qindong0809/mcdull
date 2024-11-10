package io.gitee.dqcer.blaze.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业信息 实体类
 *
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("blaze_customer_info")
public class CustomerInfoEntity extends BaseEntity<Integer> {

    /**
     * 企业名称
     */
    private String name;
    /**
     * 所在地省代码
     */
    private String provincesCode;
    /**
     * 所在市代码
     */
    private String cityCode;
    /**
     * 联系人
     */
    private String contactPerson;
    /**
     * 联系电话
     */
    private String phoneNumber;
    /**
     * 客户类型
     */
    private String customerType;
    /**
     * 备注
     */
    private String remark;
}