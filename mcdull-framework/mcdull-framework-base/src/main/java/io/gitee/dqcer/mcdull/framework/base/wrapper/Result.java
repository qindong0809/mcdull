package io.gitee.dqcer.mcdull.framework.base.wrapper;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

/**
 * 统一返回前端包装类
 *
 * @author dqcer
 * @since 2022/07/26
 */
public class Result<T> extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * message
     */
    private String msg;

    /**
     * 数据对象
     */
    private T data;


    /**
     * 初始化一个新创建的 Result 对象，使其表示一个空消息。
     */
    private Result() {
    }


    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> Result<T> success() {
        return Result.<T>builder()
                .withCode(CodeEnum.SUCCESS.code)
                .withMessage(CodeEnum.SUCCESS.message)
                .withData(null)
                .build();
    }

    /**
     * 返回成功数据
     *
     * @param data 数据
     * @return 成功消息
     */
    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .withCode(CodeEnum.SUCCESS.code)
                .withMessage(CodeEnum.SUCCESS.message)
                .withData(data)
                .build();
    }


    /**
     * 返回错误消息
     *
     * @param resultCode 结果代码
     * @return 警告消息
     */
    public static <T> Result<T> error(ICode resultCode) {
        return Result.<T>builder()
                .withCode(resultCode.getCode())
                .withMessage(resultCode.getMessage())
                .build();
    }

    /**
     * 错误
     *
     * @param message 消息
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> error(String message) {
        return Result.<T>builder()
                .withCode(CodeEnum.INTERNAL_SERVER_ERROR.getCode())
                .withMessage(MessageFormat.format(CodeEnum.INTERNAL_SERVER_ERROR.getMessage(), message))
                .build();
    }

    /**
     * 返回错误消息
     *
     * @param resultCode 结果代码
     * @param param      参数
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> error(ICode resultCode, List<Object> param) {
        return Result.<T>builder()
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
    public static <T> Result<T> error(int resultCode, String msg) {
        return Result.<T>builder()
                .withCode(resultCode)
                .withMessage(msg)
                .build();
    }

    @Override
    public Result<T> put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 是否成功
     *
     * @return boolean
     */
    @JsonIgnore
    public boolean isOk() {
        return code == CodeEnum.SUCCESS.code;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Result.class.getSimpleName() + "[", "]")
                .add("code=" + code)
                .add("message='" + msg + "'")
                .add("data=" + data)
                .toString();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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
        public Result<T> build() {
            Result<T> result = new Result<>();
            result.setCode(code);
            result.setMsg(message);
            result.setData(data);
            return result;
        }
    }

}

