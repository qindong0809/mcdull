package io.gitee.dqcer.mcdull.workflow.facade.model.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量查询结果。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
public class BatchQueryResult<K, V> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Builder.Default
    private List<BatchQueryItem<K, V>> items = new ArrayList<>();

    @Builder.Default
    private List<K> missingKeys = new ArrayList<>();

    public static <K, V> BatchQueryResult<K, V> empty() {
        return BatchQueryResult.<K, V>builder().build();
    }

    public void add(K key, V value) {
        if (value == null) {
            missingKeys.add(key);
            return;
        }
        items.add(BatchQueryItem.<K, V>builder().key(key).value(value).build());
    }
}
