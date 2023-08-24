package io.gitee.dqcer.mcdull.admin.web.service.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceListDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.InstanceVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.vo.SelectOptionVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

import java.util.List;

/**
 * 系统配置 接口定义
 *
 * @author dqcer
 * @since 2023/01/15 15:01:98
 */
public interface IInstanceService {

    Result<PagedVO<InstanceVO>> list(InstanceListDTO dto);

    Result<InstanceVO> detail(Long id);

    Result<Long> add(InstanceAddDTO dto);

    Result<Long> edit(InstanceEditDTO dto);

    Result<Long> remove(Long id);

    Result<List<SelectOptionVO<Long>>> baseInfoListByGroupId(Long groupId);

    Result<Boolean> testConnect(InstanceAddDTO dto);

    Result<List<SelectOptionVO<Long>>> backList();

}
