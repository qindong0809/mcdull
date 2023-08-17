package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;

import java.util.StringJoiner;

/**
 * 更新状态
 *
 * @author dqcer
 * @since 2022/12/07
 */
public class StatusDTO extends PkDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 状态
     * @see StatusEnum
     */
    @EnumsStrValid(required = true, value = StatusEnum.class)
    private String status;

    @Override
    public String toString() {
        return new StringJoiner(", ", StatusDTO.class.getSimpleName() + "[", "]")
                .add("status='" + status + "'")
                .toString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
