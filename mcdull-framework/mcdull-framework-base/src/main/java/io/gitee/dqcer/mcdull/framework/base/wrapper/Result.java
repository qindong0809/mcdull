package io.gitee.dqcer.mcdull.framework.base.wrapper;


import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.StringJoiner;

/**
 * 统一返回前端包装类
 *
 * @author dqcer
 * @since 2022/07/26
 */
public class Result<T> implements Serializable {

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

    /**
     * Trace Id
     */
    private String traceId;

    /**
     * ok
     */
    private Boolean ok;

    /**
     * exception
     */
    private String exception;


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
                .withMessage(message)
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
    public static <T> Result<T> error(int resultCode, String msg, String exception) {
        return Result.<T>builder()
                .withCode(resultCode)
                .withMessage(msg)
                .withException(exception)
                .build();
    }


    /**
     * 是否成功
     *
     * @return boolean
     */
//    @JsonIgnore
    public boolean isOk() {
        return code == CodeEnum.SUCCESS.code;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Result.class.getSimpleName() + "[", "]")
                .add("code=" + code)
                .add("message='" + message + "'")
                .add("data=" + data)
                .add("traceId='" + traceId + "'")
                .add("ok=" + ok)
                .add("exception='" + exception + "'")
                .toString();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTraceId() {
        UnifySession session = UserContextHolder.getSession();
        if (ObjUtil.isNotNull(session)) {
            return session.getTraceId();
        }
        return StrUtil.EMPTY;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public static <T> ResultBuilder<T> builder() {
        return new ResultBuilder<>();
    }

    public static final class ResultBuilder<T> {

        private int code;

        private String message;

        private T data;

        private String exception;

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

        public ResultBuilder<T> withException(String exception) {
            this.exception = exception;
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
            result.setMessage(message);
            result.setData(data);
            result.setException(exception);
            return result;
        }
    }

}

