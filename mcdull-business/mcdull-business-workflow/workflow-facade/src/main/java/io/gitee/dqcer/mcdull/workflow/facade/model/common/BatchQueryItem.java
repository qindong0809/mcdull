package io.gitee.dqcer.mcdull.workflow.facade.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 批量查询中的单条结果，避免 JSON 中使用复杂对象作为 Map key。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchQueryItem<K, V> implements Serializable {

    private static final long serialVersionUID = 1L;

    private K key;

    private V value;
}
