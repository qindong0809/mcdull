package io.gitee.dqcer.mcdull.system.provider.model.vo.administrator;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.List;

@Data
public class RolePermissionVO implements VO {

    private Integer id;

    private String title;

    private Integer type;

    private String permission;

    private Integer parentId;

    private List<RolePermissionVO> children;
}
