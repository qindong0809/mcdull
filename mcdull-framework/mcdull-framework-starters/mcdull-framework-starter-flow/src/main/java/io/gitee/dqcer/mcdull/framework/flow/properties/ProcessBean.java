package io.gitee.dqcer.mcdull.framework.flow.properties;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 过程
 *
 * @author dqcer
 * @since 2023/01/08 17:01:92
 */
public class ProcessBean implements Serializable {

    /**
     * 业务唯一标识
     */
    private String id;

    /**
     * 中文描述
     */
    private String remark;

    private List<String> nodeList;

    @Override
    public boolean equals(Object object) {
        if (this == object) {

            return true;
        }
        if (object == null || getClass() != object.getClass()) {

            return false;
        }

        ProcessBean that = (ProcessBean) object;

        if (!Objects.equals(id, that.id)) {

            return false;
        }
        if (!Objects.equals(remark, that.remark)) {

            return false;
        }
        return Objects.equals(nodeList, that.nodeList);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (nodeList != null ? nodeList.hashCode() : 0);
        return result;
    }

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
