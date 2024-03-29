package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DeptLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DeptVO;
import io.gitee.dqcer.mcdull.framework.base.vo.TreeSelectVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

import java.util.List;

/**
 * 部门服务 接口定义
 *
 * @author dqcer
 * @since 2023/01/15 15:01:98
 */
public interface IDeptService {

    Result<List<DeptVO>> list(DeptLiteDTO dto);

    Result<List<DeptVO>> excludeChild(Long deptId);

    Result<DeptVO> detail(Long deptId);

    Result<List<TreeSelectVO>> selectDeptTreeList();

    Result<String> add(DeptVO deptVO);

    Result<String> update(DeptVO deptVO);
}
