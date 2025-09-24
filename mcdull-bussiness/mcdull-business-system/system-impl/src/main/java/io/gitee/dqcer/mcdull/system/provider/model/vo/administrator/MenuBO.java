package io.gitee.dqcer.mcdull.system.provider.model.vo.administrator;

import java.util.List;

public class MenuBO {

    private Integer id;

    private String name;

    private String code;

    private Integer parentId;

    private List<MenuBO> children;
}
