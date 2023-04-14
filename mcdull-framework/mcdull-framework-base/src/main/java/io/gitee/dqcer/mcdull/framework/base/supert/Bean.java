package io.gitee.dqcer.mcdull.framework.base.supert;

import java.io.Serializable;

/**
 * bean
 *
 * @author dqcer
 * @since 2023/04/13
 */
public interface Bean<Id, Name> extends Serializable {

    /**
     * key
     *
     * @return {@link Id}
     */
    Id getId();

    /**
     * value
     *
     * @return {@link Name}
     */
    Name getName();
}
