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
*
* @author dqcer
* @since 2024-04-29
*/
public interface IHelpDocService {


    HelpDocDetailVO view(Long helpDocId);

    List<HelpDocVO> queryAllHelpDocList();

    PagedVO<HelpDocViewRecordVO> queryViewRecord(HelpDocViewRecordQueryDTO dto);

    PagedVO<HelpDocVO> query(HelpDocQueryDTO dto);

    HelpDocDetailVO getDetail(Long helpDocId);

    void add(HelpDocAddDTO dto);

    void update(HelpDocUpdateDTO dto);

    void delete(Long helpDocId);

    List<HelpDocVO> queryHelpDocByRelationId(Long relationId);
}
