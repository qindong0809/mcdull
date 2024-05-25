package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentTreeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentVO;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IDepartmentService {


    List<DepartmentVO> list(DeptListDTO dto);

    List<DepartmentVO> getAll();

    boolean insert(DeptInsertDTO dto);

    boolean update(Integer id, DeptUpdateDTO dto);

    boolean delete(Integer id);

    List<DepartmentTreeVO> departmentTree();

    Map<Integer, String> getNameMap(List<Integer> idList);

    List<Integer> getChildrenIdList(Integer departmentId);
}
