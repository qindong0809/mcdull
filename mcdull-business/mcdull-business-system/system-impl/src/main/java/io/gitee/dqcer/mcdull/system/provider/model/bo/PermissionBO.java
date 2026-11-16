package io.gitee.dqcer.mcdull.system.provider.model.bo;

import io.gitee.dqcer.mcdull.framework.base.support.BO;
import lombok.Data;

import java.util.List;

@Data
public class PermissionBO implements BO {

    private Integer id;

    private String name;

    private String code;

    private Integer parentId;

    private List<PermissionBO> children;
}
