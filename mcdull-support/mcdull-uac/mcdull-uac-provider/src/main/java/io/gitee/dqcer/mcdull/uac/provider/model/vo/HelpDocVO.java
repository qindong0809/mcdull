package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帮助文档
 *
 */
@Data
public class HelpDocVO implements VO {

    @Schema(description = "id")
    private Integer helpDocId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "分类")
    private Integer helpDocCatalogId;

    @Schema(description = "分类名称")
    private String helpDocCatalogName;

    @Schema(description = "作者")
    private String author;
    
    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "页面浏览量")
    private Integer pageViewCount;

    @Schema(description = "用户浏览量")
    private Integer userViewCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
