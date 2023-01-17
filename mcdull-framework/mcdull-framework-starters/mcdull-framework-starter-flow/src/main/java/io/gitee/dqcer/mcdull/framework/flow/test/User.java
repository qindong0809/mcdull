package io.gitee.dqcer.mcdull.framework.flow.test;


import java.io.Serializable;

/**
 * @author Kevin Liu
 * @date 2020/5/4 10:42 上午
 */

public class User implements Serializable {

    private long id;

    private String name;

    private int age;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
