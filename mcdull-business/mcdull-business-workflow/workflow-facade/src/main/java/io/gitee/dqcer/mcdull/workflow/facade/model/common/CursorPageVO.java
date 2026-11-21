package io.gitee.dqcer.mcdull.workflow.facade.model.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 游标分页结果。count 是可选字段，调用方不需要总数时不触发 count 查询。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
public class CursorPageVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Builder.Default
    private List<T> list = new ArrayList<>();

    private String nextCursor;

    private boolean hasMore;

    private Long total;
}
