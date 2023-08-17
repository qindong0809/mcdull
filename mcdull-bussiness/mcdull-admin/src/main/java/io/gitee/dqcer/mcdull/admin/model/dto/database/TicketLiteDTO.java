package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
*  接收客户端参数
*
* @author dqcer
* @since 2023-08-17
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

     /**
      * 主键
      */
     @NotNull(groups = {ValidGroup.Update.class, ValidGroup.One.class, ValidGroup.Status.class})
     private Long id;
    /**
     * 名称
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String name;

    /**
     * 工单号
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String number;

    /**
     * 组号
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    private Long groupId;

    /**
     * 执行方式（1/停放执行 2/在线执行）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsIntValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private Integer executeType;

    /**
     * 是否合入初始化脚本（1/是 2/否）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsIntValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private Integer hasMerge;

    /**
     * 备注
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String remark;

    /**
     * sql 脚本
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String sqlScript;

    /**
     * 流程状态（1可编辑 2已发布 3已通过 4已执行）
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private Integer followStatus;

    /**
     * 回退状态（1正常 2dev:撤回 3test:不通过 4prod:驳回）
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private Integer cancelStatus;

    /**
     * 状态（1正常 2停用）
     */
     @NotNull(groups = {ValidGroup.Status.class})
     private String status;
}