package io.gitee.dqcer.mcdull.framework.web.feign.model;

import java.io.Serializable;

/**
 * 用户会话
 *
 * @author dqcer
 * @since 2023/03/26
 */
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Integer type;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
