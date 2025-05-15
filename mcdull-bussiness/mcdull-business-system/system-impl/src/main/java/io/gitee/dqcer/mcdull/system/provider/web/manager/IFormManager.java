package io.gitee.dqcer.mcdull.system.provider.web.manager;

import cn.hutool.json.JSONObject;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FormRecordDataVO;

import java.util.List;
import java.util.Map;

/**
 * 动态表单通用逻辑定义
 *
 * @author dqcer
 * @since 2024/06/17
 */
public interface IFormManager {

    /**
     * 根据json解析获取item list
     *
     * @param jsonText json文本
     * @return {@link List}<{@link JSONObject}>
     */
    List<JSONObject> getFormItemList(String jsonText);

    /**
     * 初始化表单和表单项
     *
     * @param formId   表单id
     * @param jsonText json文本
     */
    void initFormAndFormItem(Integer formId, String jsonText);

    /**
     * 添加表单记录数据
     *
     * @param formId   表单id
     * @param formData 表单数据
     */
    void addFormRecordData(Integer formId, Map<String, Object> formData);

    /**
     * 记录列表
     *
     * @param formId 表单id
     * @return {@link List}<{@link FormRecordDataVO}>
     */
    List<FormRecordDataVO> recordList(Integer formId);

    /**
     * 表单配置 ready
     *
     * @param formId 表单id
     */
    void formConfigReady(Integer formId);

    /**
     * 删除表单记录
     *
     * @param recordId 记录id
     */
    void deleteOneRecord(Integer recordId);

    void updateOneRecord(Integer recordId, Map<String, Object> formData);

    Map<String, Object> getOneRecordNoConvert(Integer recordId);
}
