package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ConfigVO;

import java.util.List;

/**
* 系统配置 业务接口类
*
* @author dqcer
* @since 2024-04-29
*/
public interface IConfigService {


    PagedVO<ConfigVO> queryPage(ConfigQueryDTO dto);

    void add(ConfigAddDTO dto);

    void update(ConfigUpdateDTO dto);

    void delete(List<Integer> idList);
}
