package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormItemVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormVO;

import java.util.List;
import java.util.Map;

public interface IFormService {

    PagedVO<FormVO> queryPage(FormQueryDTO dto);

    void add(FormAddDTO dto);

    void update(FormUpdateDTO dto);

    void delete(Integer id);

    void updateJsonText(FormUpdateJsonTextDTO dto);

    void formConfigReady(Integer formId);

    FormVO detail(Integer formId);

    List<FormItemVO> itemConfigList(Integer formId);

    void recordAdd(FormRecordAddDTO dto);

    PagedVO<Map<String, String>> recordQueryPage(FormRecordQueryDTO dto);

    void  exportData(FormRecordQueryDTO dto);

    void deleteOneRecord(Integer recordId);

    void updateOneRecord(FormRecordUpdateDTO dto);

    Map<String, Object> getOneRecordNoConvert(Integer recordId);
}
