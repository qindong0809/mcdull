package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ConfigInfoVO;

import java.util.List;

/**
 * Config service
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IConfigService {


    PagedVO<ConfigInfoVO> queryPage(ConfigQueryDTO dto);

    void add(ConfigAddDTO dto);

    void update(ConfigUpdateDTO dto);

    void delete(List<Integer> idList);

    void exportData(ConfigQueryDTO dto);
}
