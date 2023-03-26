package io.gitee.dqcer.mcdull.framework.base.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.StringJoiner;

/**
 * 树选择视图对象
 *
 * @author dqcer
 * @since 2023/03/26
 */
public class TreeSelectVO implements VO{

    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelectVO> children;

    @Override
    public String toString() {
        return new StringJoiner(", ", TreeSelectVO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("label='" + label + "'")
                .add("children=" + children)
                .toString();
    }

    public Long getId() {
        return id;
    }

    public TreeSelectVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public TreeSelectVO setLabel(String label) {
        this.label = label;
        return this;
    }

    public List<TreeSelectVO> getChildren() {
        return children;
    }

    public TreeSelectVO setChildren(List<TreeSelectVO> children) {
        this.children = children;
        return this;
    }
}
