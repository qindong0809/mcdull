package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocCatalogAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocCatalogUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocCatalogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocCatalogVO;

import java.util.List;

/**
 * Help Doc Catalog Service
 *
 * @author dqcer
 * @since 2024/7/25 9:23
 */

public interface IHelpDocCatalogService {

    List<HelpDocCatalogVO> getAll();

    void add(HelpDocCatalogAddDTO dto);

    void update(HelpDocCatalogUpdateDTO dto);

    void delete(Integer id);

    HelpDocCatalogEntity getById(Integer id);

    List<HelpDocCatalogEntity> queryListByIds(List<Integer> idList);
}
