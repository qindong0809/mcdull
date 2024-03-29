package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;

import java.util.StringJoiner;

/**
 * 字典类型 实体
 *
 * @author dqcer
 * @since 2022/11/01 22:11:06
 */
@TableName("sys_dict_type")
public class DictTypeEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
    private String status;

    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return new StringJoiner(", ", DictTypeEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("dictName='" + dictName + "'")
                .add("dictType='" + dictType + "'")
                .add("status=" + status)
                .add("remark='" + remark + "'")
                .toString();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
