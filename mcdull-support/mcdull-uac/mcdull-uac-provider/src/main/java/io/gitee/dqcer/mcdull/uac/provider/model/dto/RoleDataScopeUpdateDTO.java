package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色的数据范围更新
 *
 * @author dqcer
 * @since 2024/05/13
 */
@Data
public class RoleDataScopeUpdateDTO implements DTO {

    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    private Integer roleId;

    @Schema(description = "设置信息")
    @Valid
    private List<RoleUpdateDataScopeListFormItem> dataScopeItemList;


    @Data
    public static class RoleUpdateDataScopeListFormItem {

        @Schema(description = "数据范围类型")
        @NotNull(message = "数据范围类型不能为空")
        private Integer dataScopeType;

        @Schema(description = "可见范围")
        @NotNull(message = "可见范围不能为空")
        private Integer viewType;
    }

}
