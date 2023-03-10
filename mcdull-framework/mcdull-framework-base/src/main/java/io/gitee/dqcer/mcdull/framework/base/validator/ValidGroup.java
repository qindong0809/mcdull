package io.gitee.dqcer.mcdull.framework.base.validator;

/**
 * 效验组 @NotNull/基本类型、@NotBlank/字符串、@Length/字符串长度、@NotEmpty/集合
 *
 * @author dqcer
 * @since 2022/01/11
 */
@SuppressWarnings("unused")
public interface ValidGroup {

    /**
     * 保存
     */
    interface Insert { }

    /**
     * 更新
     */
    interface Update { }

    /**
     * 查询
     */
    interface Query { }

    /**
     * 删除
     */
    interface Delete { }

    /**
     * 状态更新
     */
    interface Status { }

    /**
     * 导入
     */
    interface Import { }

    /**
     * 导出
     */
    interface Export { }

    /**
     * 分页
     */
    interface Paged { }

    /**
     * 关键字
     */
    interface Keyword { }

    /**
     * id
     */
    interface Id {}

    /**
     * 单个
     */
    interface One {}

    /**
     * 多个
     */
    interface List {}

}
