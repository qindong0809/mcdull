package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Getter;

/**
 * 码表 VO
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Getter
public class RemoteDictTypeVO implements VO {

    private Integer id;

    private String dictName;

    private String dictType;

    private String remark;

    private Boolean inactive;

    public RemoteDictTypeVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public RemoteDictTypeVO setDictName(String dictName) {
        this.dictName = dictName;
        return this;
    }

    public RemoteDictTypeVO setDictType(String dictType) {
        this.dictType = dictType;
        return this;
    }

    public RemoteDictTypeVO setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public RemoteDictTypeVO setInactive(Boolean inactive) {
        this.inactive = inactive;
        return this;
    }
}
