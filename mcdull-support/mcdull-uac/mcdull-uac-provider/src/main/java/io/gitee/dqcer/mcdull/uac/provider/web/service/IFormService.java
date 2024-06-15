package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FormAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FormQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FormUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FormUpdateJsonTextDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormItemVO;
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
}
