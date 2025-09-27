package io.gitee.dqcer.mcdull.system.provider.model.vo.administrator;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.List;

@Data
public class AdminDeptTreeVO implements VO {

    private Integer id;
    private Integer parentId;
    private String title;
    private Integer sort;
    private List<AdminDeptTreeVO> children;
}
