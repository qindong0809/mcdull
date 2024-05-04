package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 帮助文档的 目录
 *
 */
@Data
public class HelpDocCatalogVO implements VO {

    @Schema(description = "帮助文档目录id")
    private Integer helpDocCatalogId;

    @Schema(description = "帮助文档目录-名称")
    private String name;

    @Schema(description = "帮助文档目录-父级id")
    private Integer parentId;

    @Schema(description = "帮助文档目录-排序")
    private Integer sort;

}
