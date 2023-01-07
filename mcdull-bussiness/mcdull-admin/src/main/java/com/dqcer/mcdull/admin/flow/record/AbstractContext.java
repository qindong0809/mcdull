package com.dqcer.mcdull.admin.flow.record;


import com.dqcer.mcdull.admin.flow.node.Context;


public class AbstractContext implements Context {

    protected  String id;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
