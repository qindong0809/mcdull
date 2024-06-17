package io.gitee.dqcer.mcdull.uac.provider.web.manager;

import cn.hutool.json.JSONObject;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormRecordDataVO;

import java.util.List;
import java.util.Map;

/**
 * 动态表单通用逻辑定义
 *
 * @author dqcer
 * @since 2024/06/17
 */
public interface IFormManager {

    List<JSONObject> getFormItemList(String jsonText);

    /**
     * 初始化表单和表单项
     *
     * @param formId   表单id
     * @param jsonText json文本
     */
    void initFormAndFormItem(Integer formId, String jsonText);

    void addFormRecordData(Integer formId, Map<String, String> formData);

    List<FormRecordDataVO> recordList(Integer formId);
}
