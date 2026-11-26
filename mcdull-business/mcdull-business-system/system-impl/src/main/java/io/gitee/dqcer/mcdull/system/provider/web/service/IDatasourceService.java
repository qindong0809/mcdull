package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceTestDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DatasourceEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceOptionVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceTestVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceVO;

import java.util.List;

public interface IDatasourceService {
    PagedVO<DatasourceVO> queryPage(DatasourceQueryDTO dto);

    void add(DatasourceAddDTO dto);

    void update(DatasourceUpdateDTO dto);

    void changeStatus(Integer id, Boolean inactive);

    void delete(Integer id);

    List<DatasourceOptionVO> options();

    DatasourceTestVO test(DatasourceTestDTO dto);

    DatasourceEntity getEntity(Integer id);
}
