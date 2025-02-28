package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ChangeLogAndVersionVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ChangeLogVO;

import java.util.List;

/**
 * Change log service
 *
 * @author dqcer
 * @since 2024/7/25 9:19
 */

public interface IChangeLogService {

    boolean add(ChangeLogAddDTO addForm);

    void update(ChangeLogUpdateDTO logUpdateForm);

    void batchDelete(List<Integer> idList);

    PagedVO<ChangeLogVO> queryPage(ChangeLogQueryDTO dto);

    ChangeLogVO getById(Integer id);

    ChangeLogAndVersionVO getChangeLogAndVersion();

    void exportData(ChangeLogQueryDTO dto);
}
