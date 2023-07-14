package io.gitee.dqcer.mcdull.admin.web.service.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupListDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.GroupVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

import java.util.List;

/**
 * 系统配置 接口定义
 *
 * @author dqcer
 * @since 2023/01/15 15:01:98
 */
public interface IGroupService {

    Result<PagedVO<GroupVO>> list(GroupListDTO dto);

    Result<GroupVO> detail(Long id);

    Result<Long> add(GroupAddDTO dto);

    Result<Long> edit(GroupEditDTO dto);

    Result<Long> remove(Long id);

    Result<List<GroupVO>> allList();
}
