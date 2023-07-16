package io.gitee.dqcer.mcdull.admin.web.service.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.GitAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GitEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GitListDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GitVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

import java.util.List;

/**
 * 系统配置 接口定义
 *
 * @author dqcer
 * @since 2023/01/15 15:01:98
 */
public interface IGitService {

    Result<PagedVO<GitVO>> list(GitListDTO dto);

    Result<GitVO> detail(Long id);

    Result<Long> add(GitAddDTO dto);

    Result<Long> edit(GitEditDTO dto);

    Result<Long> remove(Long id);

    Result<List<GitVO>> allList();
}
