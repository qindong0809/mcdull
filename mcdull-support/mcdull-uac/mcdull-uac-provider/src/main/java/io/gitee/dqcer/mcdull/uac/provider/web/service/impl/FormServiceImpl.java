package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormItemVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormRecordDataVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IFormManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@Service
public class FormServiceImpl
        extends BasicServiceImpl<IFormRepository> implements IFormService {

    @Resource
    private IFormManager formManager;

    @Override
    public PagedVO<FormVO> queryPage(FormQueryDTO dto) {
        List<FormVO> voList = new ArrayList<>();
        Page<FormEntity> entityPage = baseRepository.selectPage(dto);
        List<FormEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            for (FormEntity entity : recordList) {
                FormVO vo = this.convertToVO(entity);
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(FormAddDTO dto) {
        FormEntity entity = baseRepository.getByName(dto.getName());
        if (ObjUtil.isNotNull(entity)) {
            this.throwDataExistException(dto.getName());
        }
        baseRepository.save(this.convertToEntity(dto));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(FormUpdateDTO dto) {
        FormEntity form = baseRepository.getByName(dto.getName());
        if (ObjUtil.isNotNull(form)) {
            if (!form.getId().equals(dto.getId())) {
                this.throwDataExistException(dto.getName());
            }
        }
        FormEntity entity = this.convertToEntity(dto);
        entity.setId(dto.getId());
        baseRepository.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        FormEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        baseRepository.removeById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateJsonText(FormUpdateJsonTextDTO dto) {
        formManager.initFormAndFormItem(dto.getId(), dto.getJsonText());
    }

    @Override
    public FormVO detail(Integer formId) {
        FormEntity entity = baseRepository.getById(formId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(formId);
        }
        return this.convertToVO(entity);
    }

    @Override
    public List<FormItemVO> itemConfigList(Integer formId) {
        FormEntity entity = baseRepository.getById(formId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(formId);
        }
//        if (BooleanUtil.isFalse(entity.getPublish())) {
//            return Collections.emptyList();
//        }
        String jsonText = entity.getJsonText();
        if (StrUtil.isBlank(jsonText)) {
            return Collections.emptyList();
        }

        List<JSONObject> formItemList = formManager.getFormItemList(jsonText);
        if (CollUtil.isEmpty(formItemList)) {
            return Collections.emptyList();
        }
        List<FormItemVO> list = new ArrayList<>();
        for (JSONObject jsonObject : formItemList) {
            if (ObjUtil.isNotNull(jsonObject)) {
                String title = jsonObject.get("label", String.class);
                String field = jsonObject.get("field", String.class);
                if (StrUtil.isAllNotBlank(title, field)) {
                    FormItemVO itemVO = new FormItemVO();
                    itemVO.setName(title);
                    itemVO.setKey(field);
                    list.add(itemVO);
                }
            }
        }
        return list;
    }

    @Override
    public void recordAdd(FormRecordAddDTO dto) {
        formManager.addFormRecordData(dto.getFormId(), dto.getFormData());
    }

    @Override
    public PagedVO<FormRecordDataVO> recordQueryPage(FormRecordQueryDTO dto) {
        List<FormRecordDataVO> list = formManager.recordList(dto.getFormId());
        return PageUtil.ofSub(list, dto);
    }

    private FormEntity convertToEntity(FormAddDTO dto) {
        FormEntity formEntity = new FormEntity();
        formEntity.setName(dto.getName());
        formEntity.setRemark(dto.getRemark());
        formEntity.setPublish(false);
        return formEntity;
    }

    private FormVO convertToVO(FormEntity item){
        FormVO formVO = new FormVO();
        formVO.setId(item.getId());
        formVO.setName(item.getName());
        formVO.setJsonText(item.getJsonText());
        formVO.setPublish(item.getPublish());
        formVO.setRemark(item.getRemark());
        formVO.setCreateTime(LocalDateTimeUtil.of(item.getCreatedTime()));
        formVO.setUpdateTime(LocalDateTimeUtil.of(item.getUpdatedTime()));
        return formVO;
    }


}
