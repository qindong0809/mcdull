package io.gitee.dqcer.mcdull.framework.flow.test;


import io.gitee.dqcer.mcdull.framework.flow.node.Context;

/**
 * 将需要传递的值放入context中
 *
 * @author dqcer
 * @since 2023/01/08 13:01:42
 */
public class SimpleContext implements Context {

    private String id;

    private long userId;

    private User user;

    @Override
    public String getId() {
        return id;
    }

    public SimpleContext setId(String id) {
        this.id = id;
        return this;
    }

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
