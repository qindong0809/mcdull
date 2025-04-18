package io.gitee.dqcer.mcdull.system.client.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用户视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Setter
@Getter
public class RemoteUserVO implements VO {

    private Integer id;

    private Integer departmentId;

    private List<Integer> roleIdList;


}
