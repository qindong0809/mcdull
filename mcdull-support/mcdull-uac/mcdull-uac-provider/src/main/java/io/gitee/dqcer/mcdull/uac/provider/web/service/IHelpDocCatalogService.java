package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocCatalogAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocCatalogUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocCatalogVO;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface IHelpDocCatalogService {

    List<HelpDocCatalogVO> getAll();

    void add(HelpDocCatalogAddDTO dto);

    void update(HelpDocCatalogUpdateDTO dto);

    void delete(Long helpDocCatalogId);
}
