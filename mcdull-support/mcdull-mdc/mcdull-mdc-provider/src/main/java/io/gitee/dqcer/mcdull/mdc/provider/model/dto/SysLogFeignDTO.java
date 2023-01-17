package io.gitee.dqcer.mcdull.mdc.provider.model.dto;

import io.gitee.dqcer.mcdull.mdc.provider.model.entity.LogDO;

/**
 * 系统日志 feign dto
 *
 * @author dqcer
 * @version 2022/12/25
 */
public class SysLogFeignDTO extends LogDO {
    @Override
    public String toString() {
        return "SysLogFeignDTO{" +
                "id=" + id +
                "} " + super.toString();
    }
}
