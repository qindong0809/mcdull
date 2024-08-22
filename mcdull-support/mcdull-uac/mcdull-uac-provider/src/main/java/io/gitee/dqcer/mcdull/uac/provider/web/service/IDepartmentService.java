package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentInfoVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentTreeInfoVO;

import java.util.List;
import java.util.Map;

/**
 * Department Service
 *
 * @author dqcer
 * @since 2024/7/25 9:20
 */

public interface IDepartmentService {

    List<DepartmentInfoVO> getAll();

    boolean insert(DeptInsertDTO dto);

    boolean update(Integer id, DeptUpdateDTO dto);

    boolean delete(Integer id);

    List<DepartmentTreeInfoVO> departmentTree();

    Map<Integer, String> getNameMap(List<Integer> idList);

    List<Integer> getChildrenIdList(Integer departmentId);
}
