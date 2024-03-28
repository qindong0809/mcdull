package io.gitee.dqcer.mcdull.mdc.client.vo;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;

/**
 * sys dict
 *
 * @author dqcer
 * @since 2022/11/01 22:11:78
 */
public class DictTypeClientVO implements VO {

    private Integer id;

    private String dictName;

    private String dictType;

    private String remark;

    private Boolean inactive;

    public Integer getId() {
        return id;
    }

    public DictTypeClientVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getDictName() {
        return dictName;
    }

    public DictTypeClientVO setDictName(String dictName) {
        this.dictName = dictName;
        return this;
    }

    public String getDictType() {
        return dictType;
    }

    public DictTypeClientVO setDictType(String dictType) {
        this.dictType = dictType;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public DictTypeClientVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public DictTypeClientVO setInactive(Boolean inactive) {
        this.inactive = inactive;
        return this;
    }
}
