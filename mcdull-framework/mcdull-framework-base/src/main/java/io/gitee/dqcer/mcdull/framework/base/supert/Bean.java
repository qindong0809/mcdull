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
     * @return &lt;Id&gt;
     */
    Id getId();

    /**
     * value
     *
     * @return &lt;Name&gt;
     */
    Name getName();
}
