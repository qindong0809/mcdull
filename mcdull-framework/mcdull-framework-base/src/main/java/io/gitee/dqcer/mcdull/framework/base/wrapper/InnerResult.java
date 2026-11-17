package io.gitee.dqcer.mcdull.framework.base.wrapper;

import io.gitee.dqcer.mcdull.framework.base.exception.FeignBizException;
import io.gitee.dqcer.mcdull.framework.base.exception.FeignServiceErrorException;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * 统一服务内部包装类（生产级终版）
 * <p>
 * 1. 纯净传输契约：仅包含基础类型与纯字符串元数据，100% 免疫 JSON 序列化/反序列化崩溃。
 * 2. 极简业务体验：内置 unwrap() 系列方法，自动校验状态并还原抛出服务端原始业务异常。
 * 3. 零外部工具依赖：方法命名避开 get/is 前缀，无需引入 @JsonIgnore 即可避免多余字段序列化。
 *
 *查询单条记录（不允许为空）	UserDTO user = userClient.getById(id).unwrap();	若失败则抛出服务端原始业务异常；若 data == null 则抛出 FeignServiceErrorException
 *查询记录（允许为空）	UserDTO user = userClient.findByPhone(phone).unwrapOrNull();	若失败则抛出异常；正常查询不到数据时安全返回 null
 *增删改（无返回值）	userClient.updateUser(req).checkOk();	适用于 InnerResult<Void>，只校验是否成功，失败自动抛异常
 *函数式链式处理	userClient.findById(id).ifOkPresent(u -> doSomething(u));	成功且对象非空时自动执行消费逻辑
 *
 * @author dqcer
 * @since 2022/07/26
 */
public class InnerResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 业务数据对象
     */
    private T data;

    /**
     * 服务端抛出的异常全限定类名（用于客户端还原具体业务异常类型）
     */
    private String exceptionClass;

    /**
     * 服务端异常堆栈摘要（建议仅在非生产环境由全局拦截器填充，用于辅助排查）
     */
    private String exceptionDetail;

    /**
     * 公开无参构造，确保各类 JSON/RPC 框架正常反序列化
     */
    public InnerResult() {
    }

    // ==================== 静态工厂方法（成功） ====================

    public static <T> InnerResult<T> success() {
        return success(null);
    }

    public static <T> InnerResult<T> success(T data) {
        return InnerResult.<T>builder()
            .withCode(CodeEnum.SUCCESS.getCode())
            .withMessage(CodeEnum.SUCCESS.getMessage())
            .withData(data)
            .build();
    }

    // ==================== 静态工厂方法（失败） ====================

    public static <T> InnerResult<T> error(ICode resultCode) {
        return InnerResult.<T>builder()
            .withCode(resultCode.getCode())
            .withMessage(resultCode.getMessage())
            .build();
    }

    public static <T> InnerResult<T> error(String message) {
        return InnerResult.<T>builder()
            .withCode(CodeEnum.INTERNAL_SERVER_ERROR.getCode())
            .withMessage(formatMessage(CodeEnum.INTERNAL_SERVER_ERROR.getMessage(), message))
            .build();
    }

    public static <T> InnerResult<T> error(ICode resultCode, List<Object> param) {
        Object[] args = (param == null) ? new Object[0] : param.toArray();
        return InnerResult.<T>builder()
            .withCode(resultCode.getCode())
            .withMessage(formatMessage(resultCode.getMessage(), args))
            .build();
    }

    public static <T> InnerResult<T> error(int resultCode, String msg) {
        return InnerResult.<T>builder()
            .withCode(resultCode)
            .withMessage(msg)
            .build();
    }

    /**
     * 携带异常元数据构建错误响应（供服务端全局异常拦截器调用）
     */
    public static <T> InnerResult<T> error(int resultCode, String msg, Throwable throwable, String debugDetail) {
        return InnerResult.<T>builder()
            .withCode(resultCode)
            .withMessage(msg)
            .withExceptionClass(throwable != null ? throwable.getClass().getName() : null)
            .withExceptionDetail(debugDetail)
            .build();
    }

    public static <T> InnerResult<T> hystrixFallBack() {
        return error("hystrix fall back");
    }

    // ==================== 客户端拆包与异常还原（核心业务方法） ====================

    /**
     * 判断响应是否成功
     * 注意：此方法会被 Jackson 序列化为 "ok": true，反序列化时需确保全局配置了 FAIL_ON_UNKNOWN_PROPERTIES = false
     */
    public boolean isOk() {
        return code == CodeEnum.SUCCESS.getCode();
    }

    /**
     * 校验状态并提取数据，若 data 为 null 则抛出 FeignServiceErrorException
     */
    public T unwrap() {
        return unwrap(false);
    }

    /**
     * 校验状态并提取数据，允许 data 为 null
     */
    public T unwrapOrNull() {
        return unwrap(true);
    }

    /**
     * 仅校验调用是否成功（适用于 InnerResult<Void> 等无返回值的写操作）
     */
    public void checkOk() {
        unwrap(true);
    }

    /**
     * 将结果转换为 Optional 包装（允许 data 为 null），便于函数式判空处理
     */
    public Optional<T> toOptional() {
        return Optional.ofNullable(unwrap(true));
    }

    /**
     * 当调用成功且数据非空时，执行消费回调
     */
    public void ifOkPresent(Consumer<T> action) {
        T val = unwrap(true);
        if (val != null && action != null) {
            action.accept(val);
        }
    }

    /**
     * 核心拆包逻辑：校验状态码 -> 还原并抛出上游业务异常 -> 校验空值
     *
     * @param ignoreNull true: 允许返回 null；false: data 为 null 时抛出 FeignServiceErrorException
     * @return 解析后的业务数据
     */
    public T unwrap(boolean ignoreNull) {
        if (!isOk()) {
            throw rebuildRemoteException();
        }
        if (ignoreNull || this.data != null) {
            return this.data;
        }
        throw new FeignServiceErrorException("上游服务返回数据为 null");
    }

    /**
     * 根据 exceptionClass 反射重建服务端抛出的原始业务异常
     * 若本地无该异常类或构造不匹配，则安全降级为 FeignBizException
     */
    private RuntimeException rebuildRemoteException() {
        if (this.exceptionClass != null && !this.exceptionClass.isEmpty()) {
            try {
                Class<?> clazz = Class.forName(this.exceptionClass);
                if (RuntimeException.class.isAssignableFrom(clazz)) {
                    // 策略 1：优先匹配自定义业务异常常用的 (int code, String message) 构造方法
                    try {
                        Constructor<?> constructor = clazz.getConstructor(int.class, String.class);
                        return (RuntimeException) constructor.newInstance(this.code, this.message);
                    } catch (NoSuchMethodException ignored) {
                        // 继续尝试下一个构造方法
                    }

                    // 策略 2：匹配标准异常常用的 (String message) 构造方法
                    try {
                        Constructor<?> constructor = clazz.getConstructor(String.class);
                        return (RuntimeException) constructor.newInstance(this.message);
                    } catch (NoSuchMethodException ignored) {
                        // 继续尝试下一个构造方法
                    }

                    // 策略 3：尝试无参构造方法
                    try {
                        Constructor<?> constructor = clazz.getConstructor();
                        return (RuntimeException) constructor.newInstance();
                    } catch (NoSuchMethodException ignored) {
                        // 无法实例化，走兜底异常
                    }
                }
            } catch (Exception ignored) {
                // 本地类路径不存在该异常类，或安全管理器限制实例化时，静默降级
            }
        }
        return new FeignBizException(this);
    }

    /**
     * 安全格式化消息，避免模板中无占位符或单引号导致的异常
     */
    private static String formatMessage(String pattern, Object... arguments) {
        if (pattern == null || arguments == null || arguments.length == 0) {
            return pattern;
        }
        try {
            return MessageFormat.format(pattern, arguments);
        } catch (IllegalArgumentException e) {
            return pattern;
        }
    }

    // ==================== Getter / Setter / toString / Builder ====================

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", InnerResult.class.getSimpleName() + "[", "]")
            .add("code=" + code)
            .add("message='" + message + "'")
            .add("data=" + data);
        if (exceptionClass != null) {
            joiner.add("exceptionClass='" + exceptionClass + "'");
        }
        if (exceptionDetail != null) {
            joiner.add("exceptionDetail='" + exceptionDetail + "'");
        }
        return joiner.toString();
    }

    public int getCode() {
        return code;
    }

    public InnerResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public InnerResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public InnerResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public InnerResult<T> setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
        return this;
    }

    public String getExceptionDetail() {
        return exceptionDetail;
    }

    public InnerResult<T> setExceptionDetail(String exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
        return this;
    }

    public static <T> ResultBuilder<T> builder() {
        return new ResultBuilder<>();
    }

    public static final class ResultBuilder<T> {
        private int code;
        private String message;
        private T data;
        private String exceptionClass;
        private String exceptionDetail;

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

        public ResultBuilder<T> withExceptionClass(String exceptionClass) {
            this.exceptionClass = exceptionClass;
            return this;
        }

        public ResultBuilder<T> withExceptionDetail(String exceptionDetail) {
            this.exceptionDetail = exceptionDetail;
            return this;
        }

        public InnerResult<T> build() {
            InnerResult<T> result = new InnerResult<>();
            result.setCode(code);
            result.setMessage(message);
            result.setData(data);
            result.setExceptionClass(exceptionClass);
            result.setExceptionDetail(exceptionDetail);
            return result;
        }
    }
}
