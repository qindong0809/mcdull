package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 文件夹树信息 vo
 *
 * @author dqcer
 * @since 2024/11/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FolderTreeInfoVO extends FolderInfoVO {

    private String title;

    private String key;

    @Schema(description = "是否是叶子节点")
    private Boolean isLeaf;

    @Schema(description = "同级上一个元素id")
    private Integer preId;

    @Schema(description = "同级下一个元素id")
    private Integer nextId;

    @Schema(description = "子部门")
    private List<FolderTreeInfoVO> children;

    @Schema(description = "自己和所有递归子部门的id集合")
    private List<Integer> selfAndAllChildrenIdList;

}
