package io.gitee.dqcer.mcdull.workflow.facade.model.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量写结果。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
public class BatchResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int total;

    private int successCount;

    private int failCount;

    @Builder.Default
    private List<ItemResult<T>> items = new ArrayList<>();

    public static <T> BatchResult<T> of(List<ItemResult<T>> items) {
        List<ItemResult<T>> safe = items == null ? new ArrayList<>() : items;
        int success = 0;
        for (ItemResult<T> item : safe) {
            if (item != null && item.isSuccess()) {
                success++;
            }
        }
        return BatchResult.<T>builder()
                .total(safe.size())
                .successCount(success)
                .failCount(safe.size() - success)
                .items(safe)
                .build();
    }
}
