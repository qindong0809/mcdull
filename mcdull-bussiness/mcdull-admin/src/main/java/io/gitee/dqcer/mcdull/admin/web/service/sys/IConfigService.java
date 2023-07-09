package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.ConfigVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

/**
 * 系统配置 接口定义
 *
 * @author dqcer
 * @since 2023/01/15 15:01:98
 */
public interface IConfigService {

    Result<PagedVO<ConfigVO>> list(ConfigLiteDTO dto);

    Result<ConfigVO> detail(Long id);

    Result<Long> add(ConfigAddDTO dto);

    Result<Long> edit(ConfigEditDTO dto);

    Result<Long> remove(Long id);
}
