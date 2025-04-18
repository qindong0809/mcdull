package io.gitee.dqcer.mcdull.system.provider.model.vo.workflow;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流类别 vo
 *
 * @author dqcer
 * @since 2025/04/17
 */
@Data
public class FlowCategoryVO implements VO {

    @Schema(description = "流程分类ID")
    private Long categoryId;

    @Schema(description = "父级id")
    private Long parentId;

    @Schema(description = "父类别名称")
    private String parentName;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "流程分类名称")
    private String categoryName;

    @Schema(description = "显示顺序")
    private Long orderNum;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "子菜单")
    private List<FlowCategoryVO> children = new ArrayList<>();
}
