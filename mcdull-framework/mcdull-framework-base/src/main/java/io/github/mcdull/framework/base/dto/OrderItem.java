package io.gitee.dqcer.framework.base.dto;

import java.io.Serializable;

/**
 * 排序元素
 *
 * @author dqcer
 * @date 2023/01/06 23:01:24
 */
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 需要进行排序的字段
     */
    protected String column;

    /**
     * 是否正序排列，默认 true
     */
    protected Boolean asc;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OrderItem{");
        sb.append("column='").append(column).append('\'');
        sb.append(", asc=").append(asc);
        sb.append('}');
        return sb.toString();
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Boolean getAsc() {
        return asc;
    }

    public void setAsc(Boolean asc) {
        this.asc = asc;
    }
}
