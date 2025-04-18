package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * sys_change_log
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_oa_enterprise")
public class OaEnterpriseEntity extends BaseEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private String enterpriseName;
    private String enterpriseLogo;
    private Integer type;

    private String unifiedSocialCreditCode;

    private String contact;
    private String contactPhone;
    private String email;
    private Integer province;
    private String provinceName;
    private Integer city;
    private String cityName;
    private Integer district;
    private String districtName;
    private String address;

    private String businessLicense;
}
