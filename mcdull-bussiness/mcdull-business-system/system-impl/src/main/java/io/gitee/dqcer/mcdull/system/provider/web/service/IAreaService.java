package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.AreaEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.AreaVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.IArea;

import java.util.List;

/**
 * Area Service
 *
 * @author dqcer
 * @since 2024/7/25 9:16
 */

public interface IAreaService extends IRepository<AreaEntity> {

    PagedVO<AreaVO> queryPage(AreaQueryDTO dto);

    List<LabelValueVO<String, String>> provinceList();

    List<LabelValueVO<String, String>> cityList(String provinceCode);

    boolean exportData(AreaQueryDTO dto);

    <T extends IArea> void set(List<T> list);
}
