package io.gitee.dqcer.mcdull.system.client.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Getter;
import lombok.Setter;

/**
 * Dept
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Setter
@Getter
public class RemoteConfigVO implements VO {

    private String key;

    private String value;

}
