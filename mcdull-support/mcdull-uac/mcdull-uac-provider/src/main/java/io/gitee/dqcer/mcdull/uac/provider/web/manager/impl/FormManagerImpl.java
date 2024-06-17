package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.web.basic.GenericLogic;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormItemRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IFormManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  动态表单通用逻辑实现类
 *
 * @author dqcer
 * @since 2022/12/24
 */
@Service
public class FormManagerImpl extends GenericLogic implements IFormManager {

    @Resource
    private IFormRepository formRepository;

    @Resource
    private IFormItemRepository formItemRepository;


    public List<JSONObject> getFormItemList(String jsonText) {
        JSONObject jsonObject = JSONUtil.parseObj(jsonText);
        JSONArray objects = jsonObject.get("schemas", JSONArray.class);
        if (CollUtil.isEmpty(objects)) {
            return Collections.emptyList();
        }
        List<JSONObject> list = new ArrayList<>();
        for (Object object : objects) {
            if (ObjUtil.isNotNull(object)) {
                JSONObject subJsonObject = JSONUtil.parseObj(object);
                List<JSONObject> sub = extracted(subJsonObject);
                if (CollUtil.isNotEmpty(sub)) {
                    list.addAll(sub);
                }
            }
        }
        return list;
    }


    private static List<JSONObject> extracted(JSONObject jsonObject) {
        List<JSONObject> list = new ArrayList<>();
        Object value = jsonObject.get("children");
        if (ObjUtil.isNotNull(value)) {
            JSONArray objects = JSONUtil.parseArray(value);
            for (Object object : objects) {
                JSONObject subJsonObject = JSONUtil.parseObj(object);
                List<JSONObject> sub = extracted(subJsonObject);
                if (CollUtil.isNotEmpty(sub)) {
                    list.addAll(sub);
                }
            }
        } else {
           list.add(jsonObject);
        }
        return list;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initFormAndFormItem(Integer formId, String jsonText) {
        FormEntity entity = formRepository.getById(formId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(formId);
        }
        if (BooleanUtil.isTrue(entity.getPublish())) {
            throw new RuntimeException("动态表单已发布，不能修改");
        }
        entity.setJsonText(jsonText);
        formRepository.updateById(entity);

        formItemRepository.deleteByFormId(formId);



//        formItemRepository.batchInsert(entity.getId(), jsonText);
    }
}
