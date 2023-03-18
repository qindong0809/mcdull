package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;

import java.util.Date;
import java.util.StringJoiner;

/**
 * 部门视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
public class DictTypeVO implements VO {


    /**
     * 编码
     */
    private Long dictId;

    /**
     * 名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态
     * @see io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum
     */
    private Integer status;

    private String remark;

    private Date createdTime;

    @Override
    public String toString() {
        return new StringJoiner(", ", DictTypeVO.class.getSimpleName() + "[", "]")
                .add("dictId=" + dictId)
                .add("dictName='" + dictName + "'")
                .add("dictType='" + dictType + "'")
                .add("status=" + status)
                .add("remark='" + remark + "'")
                .add("createdTime=" + createdTime)
                .toString();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
