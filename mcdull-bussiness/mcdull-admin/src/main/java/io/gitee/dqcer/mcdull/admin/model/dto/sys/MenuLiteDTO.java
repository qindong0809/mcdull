package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;

/**
* 资源 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
public class MenuLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    @Length(groups = ValidGroup.List.class, max = 128)
    private String menuName;

    /**
     * 状态（1/正常 2/停用）
     */
     @EnumsStrValid(groups = {ValidGroup.List.class}, value = StatusEnum.class)
     private String status;

    public String getMenuName() {
        return menuName;
    }

    public MenuLiteDTO setMenuName(String menuName) {
        this.menuName = menuName;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public MenuLiteDTO setStatus(String status) {
        this.status = status;
        return this;
    }
}