package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceOptionVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantDetailVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantVO;

import java.util.List;
import java.util.Set;

public interface ITenantService {
    PagedVO<TenantVO> queryPage(TenantQueryDTO dto);

    TenantDetailVO detail(Integer id);

    void add(TenantAddDTO dto);

    void update(TenantUpdateDTO dto);

    void changeStatus(Integer id, Boolean inactive);

    void delete(Integer id);

    List<DatasourceOptionVO> datasourceOptions();

    TenantEntity getEntity(Integer id);

    Set<Integer> authorizedTenantIds(Integer userId);
}
