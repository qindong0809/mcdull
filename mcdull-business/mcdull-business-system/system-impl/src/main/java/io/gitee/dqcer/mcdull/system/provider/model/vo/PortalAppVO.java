package io.gitee.dqcer.mcdull.system.provider.model.vo;

import lombok.Data;

@Data
public class PortalAppVO {
    private String id;
    private String name;
    private String applicationCode;
    private String scope;
    private String group;
    private String badge;
    private String target;
    private Boolean favorite;
}
