package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.hutool.core.util.ObjUtil;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Objects;

/**
 * 操作消息提醒
 *
 * @author dqcer
 */
public class ResultWrapper extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 ResultWrapper 对象，使其表示一个空消息。
     */
    public ResultWrapper() {
    }

    /**
     * 初始化一个新创建的 ResultWrapper 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public ResultWrapper(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 ResultWrapper 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public ResultWrapper(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (ObjUtil.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static ResultWrapper success() {
        return ResultWrapper.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static ResultWrapper success(Object data) {
        return ResultWrapper.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static ResultWrapper success(String msg) {
        return ResultWrapper.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static ResultWrapper success(String msg, Object data) {
        return new ResultWrapper(200, msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ResultWrapper warn(String msg) {
        return ResultWrapper.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static ResultWrapper warn(String msg, Object data) {
        return new ResultWrapper(601, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return 错误消息
     */
    public static ResultWrapper error() {
        return ResultWrapper.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 错误消息
     */
    public static ResultWrapper error(String msg) {
        return ResultWrapper.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 错误消息
     */
    public static ResultWrapper error(String msg, Object data) {
        return new ResultWrapper(500, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 错误消息
     */
    public static ResultWrapper error(int code, String msg) {
        return new ResultWrapper(code, msg, null);
    }

    /**
     * 是否为成功消息
     *
     * @return 结果
     */
    public boolean isSuccess() {
        return Objects.equals(HttpStatus.OK.value(), this.get(CODE_TAG));
    }

    /**
     * 是否为警告消息
     *
     * @return 结果
     */
    public boolean isWarn() {
        return Objects.equals(601, this.get(CODE_TAG));
    }

    /**
     * 是否为错误消息
     *
     * @return 结果
     */
    public boolean isError() {
        return Objects.equals(500, this.get(CODE_TAG));
    }

    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public ResultWrapper put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
