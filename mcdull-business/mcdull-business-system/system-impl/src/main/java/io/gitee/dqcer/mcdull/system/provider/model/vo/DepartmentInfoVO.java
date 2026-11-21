package io.gitee.dqcer.mcdull.system.provider.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.json.serialize.DynamicDateSerialize;
import io.gitee.dqcer.mcdull.system.facade.model.dto.DepartmentInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 部门信息展示对象（面向前端）。
 * <p>
 * facade 中的 {@link DepartmentInfoDTO} 仅作服务间传输契约，不绑定展示格式；
 * 面向前端的接口统一返回本对象，日期按用户会话的时区与格式序列化。
 *
 * @author dqcer
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DepartmentInfoVO extends DepartmentInfoDTO {

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date updateTime;
}
