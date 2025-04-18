package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DepartmentInfoVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DepartmentTreeInfoVO;

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

    DepartmentEntity getById(Integer departmentId);
}
