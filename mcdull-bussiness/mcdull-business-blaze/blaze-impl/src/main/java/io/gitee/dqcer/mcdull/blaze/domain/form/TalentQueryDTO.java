package io.gitee.dqcer.mcdull.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 人才表 分页查询表单
 *
 * @author dqcer
 * @since 2025-01-10 19:52:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TalentQueryDTO extends PagedDTO implements PermissionDTO{

    @Schema(description = "职称 1/无 2/初级 3/中级 4/高级 5/不限")
    private String title;

    private List<Integer> responsibleUserIdList;

}