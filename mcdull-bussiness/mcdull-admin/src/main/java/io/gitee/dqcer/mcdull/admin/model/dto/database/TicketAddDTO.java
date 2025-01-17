package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
*  接收客户端参数
*
* @author dqcer
* @since 2023-08-17
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketAddDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @NotBlank
    @Length( min = 1, max = 512)
    private String name;

    /**
     * 组号
     */
    @NotNull()
    private Long groupId;

    /**
     * 执行方式（1/停放执行 2/在线执行）
     */
    @NotNull()
    @EnumsIntValid( value = StatusEnum.class)
    private Integer executeType;

    /**
     * 是否合入初始化脚本（1/是 2/否）
     */
    @NotNull()
    @EnumsIntValid( value = StatusEnum.class)
    private Integer hasMerge;

    /**
     * 备注
     */
    @NotBlank()
    @Length( min = 1, max = 512)
    private String remark;

    /**
     * sql 脚本
     */
    @NotBlank()
    @Length( min = 1, max = 102400)
    private String sqlScript;

    @NotEmpty
    private List<Long> instanceList;
}