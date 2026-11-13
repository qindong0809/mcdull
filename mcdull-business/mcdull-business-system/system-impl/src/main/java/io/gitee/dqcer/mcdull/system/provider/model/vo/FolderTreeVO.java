package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 文件夹信息
 *
 * @author dqcer
 * @since 2024/11/29
 */
@Getter
@Setter
@ToString
public class FolderTreeVO implements VO {

    private Integer id;

    private Integer pid;

    @Schema(description = "key")
    private String key;

    @Schema(description = "名称")
    private String title;

    @Schema(description = "is leaf")
    private Boolean isLeaf;


    private List<FolderTreeVO> children;

}
