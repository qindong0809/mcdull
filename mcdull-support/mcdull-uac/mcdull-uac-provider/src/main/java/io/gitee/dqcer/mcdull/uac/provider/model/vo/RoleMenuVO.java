package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class RoleMenuVO implements VO {


    private Integer id;

    private Integer menuType;

    private Integer parentId;

    private String title;

}
