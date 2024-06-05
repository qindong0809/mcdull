package io.gitee.dqcer.mcdull.framework.web.basic;

import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * basic controller
 *
 * @author dqcer
 * @since 2023/05/18
 */
@SuppressWarnings("unused")
public abstract class BasicController {

    protected Logger log = LoggerFactory.getLogger(getClass());
    protected HttpServletRequest getRequest() {
        return ServletUtil.getRequest();
    }
    protected HttpServletResponse getResponse() {
        return ServletUtil.getResponse();
    }

}
