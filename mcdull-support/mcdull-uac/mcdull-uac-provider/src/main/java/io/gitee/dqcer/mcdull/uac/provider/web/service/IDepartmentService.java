package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentVO;

import java.util.List;

/**
 * @author dqcer
 */
public interface IDepartmentService {


    List<DepartmentVO> list(DeptListDTO dto);

    List<DepartmentVO> getAll();

    boolean insert(DeptInsertDTO dto);

    boolean update(Long id, DeptUpdateDTO dto);

    boolean delete(Long id, ReasonDTO dto);

}
