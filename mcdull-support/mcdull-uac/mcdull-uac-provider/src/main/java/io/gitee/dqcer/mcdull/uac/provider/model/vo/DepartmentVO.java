package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Getter
@Setter
@ToString
public class DepartmentVO implements VO {

    private Integer departmentId;

    private String name;

    private Integer managerId;

    private Integer parentId;

    private Integer sort;

}
