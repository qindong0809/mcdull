package io.gitee.dqcer.mcdull.system.facade.vo;

import io.gitee.dqcer.mcdull.framework.spi.datasource.UserDataSource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户绑定数据源的远程传输对象。
 * <p>
 * system 服务返回给用户所属业务服务，用于建立租户级动态数据源。
 * 敏感字段（密码）仅在 inner API 与内网链路中传输。
 *
 * @author dqcer
 */
@Data
@Schema(description = "用户绑定数据源")
public class RemoteUserDataSourceVO implements UserDataSource, Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "数据源唯一标识，作为路由 lookupKey，如 tenant_42")
    private String key;

    @Schema(description = "用户所属租户 ID，用于校验请求头 tid 与用户身份是否一致")
    private String tenantId;

    @Schema(description = "JDBC URL")
    private String url;

    @Schema(description = "数据库账号")
    private String username;

    @Schema(description = "数据库密码（仅内网传输）")
    private String password;
}
