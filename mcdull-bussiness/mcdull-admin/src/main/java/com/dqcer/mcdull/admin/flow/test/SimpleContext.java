package com.dqcer.mcdull.admin.flow.test;


import com.dqcer.mcdull.admin.flow.record.AbstractContext;

/**
 * 将需要传递的值放入context中
 * simple项目测试传递一个ID
 *
 * @author Kevin Liu
 * @date 2020/5/4 9:20 上午
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
