package io.gitee.dqcer.mcdull.framework.web.basic;

import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * basic controller
 *
 * @author dqcer
 * @since 2023/05/18
 */
@SuppressWarnings("unused")
public abstract class BasicController {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    protected HttpServletRequest request;

    protected Integer getUserId() {
        return UserContextHolder.currentUserId();
    }


}
