package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.dto.DTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 资源 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Data
public class MenuAddDTO implements DTO {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    @Length(max = 128)
    private String menuName;

    /**
     * 状态（1/正常 2/停用）
     */
     @EnumsStrValid(value = StatusEnum.class)
     private String status;

}