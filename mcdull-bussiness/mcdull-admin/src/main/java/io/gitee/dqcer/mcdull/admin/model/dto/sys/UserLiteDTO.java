package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
* 用户 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Data
public class UserLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 账户
     */
    @Length(groups = {ValidGroup.Paged.class},  max = 64)
    private String account;

    /**
     * 状态（1/正常 2/停用）
     */
    @EnumsStrValid(groups = {ValidGroup.Paged.class}, value = StatusEnum.class)
    private String status;

    /**
     * 部门id
     */
    @Min(groups = {ValidGroup.Paged.class}, value = 0)
    @Max(groups = {ValidGroup.Paged.class}, value = Long.MAX_VALUE)
    private Long deptId;
}