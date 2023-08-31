package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.admin.model.enums.EnvTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.DTO;
import lombok.Data;

/**
*  接收客户端参数
*
* @author dqcer
* @since 2023-08-29
*/
@Data
public class ConfigEnvTypeDTO implements DTO {

    private static final long serialVersionUID = 1L;

    /**
     * 标识（1/dev 2/test 3/prod）
     */
    @EnumsIntValid(required = true, value = EnvTypeEnum.class)
    private Integer type;
}