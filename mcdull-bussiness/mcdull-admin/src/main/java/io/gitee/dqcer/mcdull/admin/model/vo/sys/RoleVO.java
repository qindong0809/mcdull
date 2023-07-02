package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

import java.util.Date;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class RoleVO implements VO {

    private Long roleId;

    private String code;

    private String name;

    private Date createdTime;

    private Date updatedTime;

    private String status;

    private Integer type;

}
