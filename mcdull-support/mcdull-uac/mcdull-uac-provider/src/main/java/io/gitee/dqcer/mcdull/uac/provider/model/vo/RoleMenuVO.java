package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class RoleMenuVO implements VO {

    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "菜单类型")
    private Integer menuType;

    @Schema(description = "父菜单id")
    private Integer parentId;

    @Schema(description = "菜单名称")
    private String title;

}
