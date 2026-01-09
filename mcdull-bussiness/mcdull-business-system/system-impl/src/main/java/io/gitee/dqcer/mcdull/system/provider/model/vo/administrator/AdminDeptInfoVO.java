package io.gitee.dqcer.mcdull.system.provider.model.vo.administrator;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

@Data
public class AdminDeptInfoVO implements VO {


    private Integer id;
    private Integer parentId;
    private String name;
    private Integer sort;
    private Boolean isSystem;
    private String description;
    private Integer status;

}
