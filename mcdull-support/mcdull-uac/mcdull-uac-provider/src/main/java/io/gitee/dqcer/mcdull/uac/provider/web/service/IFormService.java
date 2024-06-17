package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormItemVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormRecordDataVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormVO;

import java.util.List;

public interface IFormService {

    PagedVO<FormVO> queryPage(FormQueryDTO dto);

    void add(FormAddDTO dto);

    void update(FormUpdateDTO dto);

    void delete(Integer id);

    void updateJsonText(FormUpdateJsonTextDTO dto);

    FormVO detail(Integer formId);

    List<FormItemVO> itemConfigList(Integer formId);

    void recordAdd(FormRecordAddDTO dto);

    PagedVO<FormRecordDataVO> recordQueryPage(FormRecordQueryDTO dto);
}
