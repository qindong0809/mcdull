package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.AreaVO;

import java.util.List;

/**
 * Area Service
 *
 * @author dqcer
 * @since 2024/7/25 9:16
 */

public interface IAreaService {

    PagedVO<AreaVO> queryPage(AreaQueryDTO dto);

    List<LabelValueVO<String, String>> provinceList();

    List<LabelValueVO<String, String>> cityList(String provinceCode);

    void exportData(AreaQueryDTO dto);
}
