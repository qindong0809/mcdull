package io.gitee.dqcer.framework.flow.test;


import io.gitee.dqcer.framework.flow.record.AbstractContext;

/**
 * 将需要传递的值放入context中
 *
 * @author dqcer
 * @date 2023/01/08 13:01:42
 */
public class SimpleContext extends AbstractContext {

    private long userId;

    private User user;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
