package com.dqcer.mcdull.admin.flow.properties;

import java.io.Serializable;
import java.util.List;

public class Process implements Serializable {

    /**
     * 业务唯一标识
     */
    private String id;

    /**
     * 中文描述
     */
    private String remark;

    private List<String> nodeList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<String> nodeList) {
        this.nodeList = nodeList;
    }
}
