package io.gitee.dqcer.mcdull.blaze.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.blaze.domain.enums.*;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 证书需求表 实体类
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("blaze_talent_certificate")
public class TalentCertificateEntity extends RelEntity<Integer> implements Approve {

    private Integer talentId;

    /**
     * 证书级别
     */
    private Integer certificateLevel;
    /**
     * 专业
     */
    private Integer specialty;
    /**
     * 单位所在省
     */
    private String provincesCode;
    /**
     * 单位所在市
     */
    private String cityCode;
    /**
     * 职称 1/无 2/初级 3/中级 4/高级 5/不限
     * {@link CertificateTitleEnum}
     */
    private Integer title;
    /**
     * 初始/转注（1/无 2/初始 3/转注 4/其它）
     * {@link CertificateInitialOrTransferEnum}
     */
    private Integer initialOrTransfer;
    /**
     * 证书状态（1/正常 2/不正常）
     * {@link CertificateStatusEnum}
     */
    private Integer certificateStatus;
    /**
     * 职位合同价
     */
    private BigDecimal positionContractPrice;
    /**
     * 其他费用
     */
    private BigDecimal otherCosts;
    /**
     * 职位实际价
     */
    private BigDecimal actualPositionPrice;
    /**
     * 期限（月）
     */
    private Integer duration;
    /**
     * 招标出场（1、可出场不招标 2、不出场可招标 3、出场招标 4、项目 5、资质 6、其它情况）
     * {@link CertificateBiddingExitEnum}
     */
    private Integer biddingExit;
    /**
     * 三类人员（1、无 2、有A证 3、有B证 4、有C证 5、可考A证 6、可考B证 7、可考C证 8、不考A证 9、不考B证 10、不考C证 11、其它）
     * {@link CertificateThreePersonnerEnum}
     */
    private Integer threePersonnel;
    /**
     * 社保要求（1、无社保 2、唯一社保可转 3、唯一社保可停 4、国企社保非唯一 5、私企社保非唯一 ）
     * {@link CertificateSocialSecurityRequirementEnum}
     */
    private Integer socialSecurityRequirement;
    /**
     * 职位来源（1、企业直签 2、同行中介）
     * {@link CertificatePositionSourceEnum}
     */
    private Integer positionSource;
    /**
     * 职位标题
     */
    private String positionTitle;
    /**
     * 备注
     */
    private String remarks;

    /**
     * {@link ApproveEnum}
     */
    private Integer approve;

    private String approveRemarks;

    private Integer responsibleUserId;
}