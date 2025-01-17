package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
* 岗位 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Data
public class PostLiteDTO extends PagedDTO implements DTO {

    private static final long serialVersionUID = 1L;

     /**
      * 主键
      */
     @NotNull(groups = {ValidGroup.Update.class, ValidGroup.One.class, ValidGroup.Status.class, ValidGroup.Delete.class})
     private Long id;

    /**
     * 状态（1/正常 2/停用）
     */
     @EnumsStrValid(groups = {ValidGroup.List.class, ValidGroup.Insert.class}, value = StatusEnum.class)
     private String status;


    /**
     * 岗位编码
     */
    @NotBlank(groups = {ValidGroup.Insert.class, ValidGroup.Update.class})
    private String postCode;

    /**
     * 岗位名称
     */
    @NotBlank(groups = {ValidGroup.Insert.class, ValidGroup.Update.class})
    private String postName;

    /**
     * 显示顺序
     */
    private Integer postSort;

    /**
     * 备注
     */
    @Length(groups = {ValidGroup.Insert.class, ValidGroup.Update.class}, max = 2)
    private String remark;

}