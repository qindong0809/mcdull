package io.gitee.mcdull.tools.web.domain;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * One selectable log target in the UI.
 *
 * @author dqcer
 */
@Data
public class LogTargetVO implements VO {

    private static final long serialVersionUID = 1L;

    private String id;

    private String env;

    private String service;

    private boolean remote;
}
