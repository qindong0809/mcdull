package io.gitee.dqcer.mcdull.business.common.recommend.dto;



import java.io.Serializable;

/**
 * 关系数据
 *
 * @author dqcer
 * @since 2023/02/24
 */
public class RelateDTO<S, T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 推荐主题 id (比如用户id)
     */
    private S useId;

    /**
     * 目标id （比如：影视id、书目、物品）
     */
    private T itemId;

    /**
     * 评分指数
     */
    private Integer index;

    public S getUseId() {
        return useId;
    }

    public RelateDTO<S, T> setUseId(S useId) {
        this.useId = useId;
        return this;
    }

    public T getItemId() {
        return itemId;
    }

    public RelateDTO<S, T> setItemId(T itemId) {
        this.itemId = itemId;
        return this;
    }

    public Integer getIndex() {
        return index;
    }

    public RelateDTO<S, T> setIndex(Integer index) {
        this.index = index;
        return this;
    }

    @Override
    public String toString() {
        return "RelateDTO{" +
                "useId=" + useId +
                ", itemId=" + itemId +
                ", index=" + index +
                '}';
    }
}
