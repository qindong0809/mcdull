package io.gitee.dqcer.mcdull.framework.feign;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ICode;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.StringJoiner;

/**
 * 统一服务内部包装类
 *
 * @author dqcer
 * @since 2022/07/26
 */
public class ResultApi<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * message
     */
    private String message;

    /**
     * 数据对象
     */
    private T data;

    private Exception exception;


    /**
     * 初始化一个新创建的 Result 对象，使其表示一个空消息。
     */
    private ResultApi() {
    }


    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> ResultApi<T> success() {
        return ResultApi.<T>builder()
                .withCode(CodeEnum.SUCCESS.getCode())
                .withMessage(CodeEnum.SUCCESS.getMessage())
                .withData(null)
                .build();
    }

    /**
     * 返回成功数据
     *
     * @param data 数据
     * @return 成功消息
     */
    public static <T> ResultApi<T> success(T data) {
        return ResultApi.<T>builder()
                .withCode(CodeEnum.SUCCESS.getCode())
                .withMessage(CodeEnum.SUCCESS.getMessage())
                .withData(data)
                .build();
    }


    /**
     * 返回错误消息
     *
     * @param resultCode 结果代码
     * @return 警告消息
     */
    public static <T> ResultApi<T> error(ICode resultCode) {
        return ResultApi.<T>builder()
                .withCode(resultCode.getCode())
                .withMessage(resultCode.getMessage())
                .build();
    }

    /**
     * 错误
     *
     * @param message 消息
     * @return {@link ResultApi}<{@link T}>
     */
    public static <T> ResultApi<T> error(String message) {
        return ResultApi.<T>builder()
                .withCode(CodeEnum.INTERNAL_SERVER_ERROR.getCode())
                .withMessage(MessageFormat.format(CodeEnum.INTERNAL_SERVER_ERROR.getMessage(), message))
                .build();
    }

    /**
     * 返回错误消息
     *
     * @param resultCode 结果代码
     * @param param      参数
     * @return {@link ResultApi}<{@link T}>
     */
    public static <T> ResultApi<T> error(ICode resultCode, List<Object> param) {
        return ResultApi.<T>builder()
                .withCode(resultCode.getCode())
                .withMessage(MessageFormat.format(resultCode.getMessage(), param.toArray()))
                .build();
    }

    /**
     * 返回错误消息
     *
     * @param resultCode 结果代码
     * @return 警告消息
     */
    public static <T> ResultApi<T> error(int resultCode, String msg) {
        return ResultApi.<T>builder()
                .withCode(resultCode)
                .withMessage(msg)
                .build();
    }


    /**
     * 是否成功
     *
     * @return boolean
     */
    @JsonIgnore
    public boolean isOk() {
        return code == CodeEnum.SUCCESS.getCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ResultApi.class.getSimpleName() + "[", "]")
                .add("code=" + code)
                .add("message='" + message + "'")
                .add("data=" + data)
                .toString();
    }

    public int getCode() {
        return code;
    }

    public ResultApi<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResultApi<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultApi<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Exception getException() {
        return exception;
    }

    public ResultApi<T> setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public static <T> ResultBuilder<T> builder() {
        return new ResultBuilder<>();
    }

    public static final class ResultBuilder<T> {

        private int code;

        private String message;

        private T data;

        private ResultBuilder() {
        }

        public ResultBuilder<T> withCode(int code) {
            this.code = code;
            return this;
        }

        public ResultBuilder<T> withMessage(String errMsg) {
            this.message = errMsg;
            return this;
        }

        public ResultBuilder<T> withData(T data) {
            this.data = data;
            return this;
        }

        /**
         * Build result.
         *
         * @return result
         */
        public ResultApi<T> build() {
            ResultApi<T> result = new ResultApi<>();
            result.setCode(code);
            result.setMessage(message);
            result.setData(data);
            return result;
        }
    }

}

