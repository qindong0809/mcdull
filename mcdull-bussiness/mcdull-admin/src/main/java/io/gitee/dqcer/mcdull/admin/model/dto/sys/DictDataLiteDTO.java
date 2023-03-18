package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;

/**
 * 码表数据 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
public class DictDataLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    private String dictType;

    @Length(groups = {ValidGroup.List.class}, max = 64)
    private String dictLabel;

    @EnumsIntValid(groups = {ValidGroup.List.class}, value = StatusEnum.class)
    private Integer status;

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

}