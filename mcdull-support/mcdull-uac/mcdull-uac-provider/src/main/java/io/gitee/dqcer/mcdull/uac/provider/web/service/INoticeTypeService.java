package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ChangeLogVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeTypeVO;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface INoticeTypeService {

    List<NoticeTypeVO> getAll();

    void add(String name);

    void update(Integer id, String name);

    void delete(Integer id);
}
