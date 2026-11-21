package io.gitee.dqcer.mcdull.workflow.facade.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 批量写中的单条结果。失败项不影响同一批其他项。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String itemKey;

    private boolean success;

    private int code;

    private String message;

    private T data;

    public static <T> ItemResult<T> success(String itemKey, T data) {
        return ItemResult.<T>builder()
                .itemKey(itemKey)
                .success(true)
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ItemResult<T> fail(String itemKey, int code, String message) {
        return ItemResult.<T>builder()
                .itemKey(itemKey)
                .success(false)
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}
