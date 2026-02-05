package io.gitee.dqcer.mcdull.system.facade.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Dept
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Setter
@Getter
public class RemoteLogonUserVO implements VO {

    private Integer userId;

    private Integer tenantId;

    private List<String> permissionList;

    private List<String> roleList;

}
