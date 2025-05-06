package io.gitee.dqcer.mcdull.blaze.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人才表 实体类
 *
 * @author dqcer
 * @since 2025-01-10 19:52:20
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("blaze_talent")
public class TalentEntity  extends RelEntity<Integer> {

    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 联系电话
     */
    private String contactNumber;
    /**
     * 工作单位性质
     */
    private Integer workUnitType;
    /**
     * 社保状态
     */
    private Integer socialSecurityStatus;
    /**
     * 社保所在省
     */
    private String provincesCode;
    /**
     * 社保所在市
     */
    private String cityCode;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 职称
     */
    private Integer title;

    private Integer responsibleUserId;
}