package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionRouterVO implements VO {


    private String path;
    private String component;
    private String name;
    private MetaVO meta;

    private List<PermissionRouterVO> children;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MetaVO {
        private String title;
        private String icon;
        private Integer rank;
        private String frameSrc;
        private Boolean keepAlive;
        private List<String> roles;
        private List<String> auths;
    }

}
