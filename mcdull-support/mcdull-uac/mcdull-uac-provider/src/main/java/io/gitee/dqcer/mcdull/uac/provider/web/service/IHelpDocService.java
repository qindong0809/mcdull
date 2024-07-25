package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocViewRecordQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocDetailVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocViewRecordVO;

import java.util.List;

/**
 * Help Doc Service
 *
 * @author dqcer
 * @since 2024/7/25 9:23
 */

public interface IHelpDocService {


    HelpDocDetailVO view(Integer helpDocId);

    List<HelpDocVO> queryAllHelpDocList();

    PagedVO<HelpDocViewRecordVO> queryViewRecord(HelpDocViewRecordQueryDTO dto);

    PagedVO<HelpDocVO> query(HelpDocQueryDTO dto);

    HelpDocDetailVO getDetail(Integer helpDocId);

    void add(HelpDocAddDTO dto);

    void update(HelpDocUpdateDTO dto);

    void delete(Integer helpDocId);

    List<HelpDocVO> queryHelpDocByRelationId(Integer relationId);
}
