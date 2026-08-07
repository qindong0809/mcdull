package io.gitee.mcdull.tools.web.domain;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * One selectable log file.
 *
 * @author dqcer
 */
@Data
public class LogFileVO implements VO {

    private static final long serialVersionUID = 1L;

    private String name;

    private long size;

    private long lastModified;

    /**
     * True for the file the live tail follows.
     */
    private boolean active;
}
