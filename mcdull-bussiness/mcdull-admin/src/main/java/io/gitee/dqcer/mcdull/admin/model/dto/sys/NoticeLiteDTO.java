package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 通知公告表 接收客户端参数
*
* @author dqcer
* @since 2023-01-18
*/
@Data
public class NoticeLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

     /**
      * 主键
      */
     @NotNull(groups = {ValidGroup.Update.class, ValidGroup.One.class, ValidGroup.Status.class})
     private Long id;
    /**
     * 公告标题
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String noticeTitle;

    /**
     * 公告内容
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 2048)
    private String noticeContent;

    /**
     * 公告类型（1通知 2公告）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    private String noticeType;

    /**
     * 状态（1/正常 2/停用）
     */
     @NotNull(groups = {ValidGroup.Status.class})
     private String status;

    /**
     * 备注
     */
    @Length(groups = {ValidGroup.Insert.class}, max = 2048)
    private String remark;
}