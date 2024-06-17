package io.gitee.dqcer.mcdull.uac.provider.web.manager;

/**
 * 动态表单通用逻辑定义
 *
 * @author dqcer
 * @since 2024/06/17
 */
public interface IFormManager {

    /**
     * 初始化表单和表单项
     *
     * @param formId   表单id
     * @param jsonText json文本
     */
    void initFormAndFormItem(Integer formId, String jsonText);
}
