package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;

/**
 * 码表 VO
 *
 * @author dqcer
 * @since 2022/12/26
 */
public class RemoteDictTypeVO implements VO {

    private Integer id;

    private String dictName;

    private String dictType;

    private String remark;

    private Boolean inactive;

    public Integer getId() {
        return id;
    }

    public RemoteDictTypeVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getDictName() {
        return dictName;
    }

    public RemoteDictTypeVO setDictName(String dictName) {
        this.dictName = dictName;
        return this;
    }

    public String getDictType() {
        return dictType;
    }

    public RemoteDictTypeVO setDictType(String dictType) {
        this.dictType = dictType;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public RemoteDictTypeVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public RemoteDictTypeVO setInactive(Boolean inactive) {
        this.inactive = inactive;
        return this;
    }
}
