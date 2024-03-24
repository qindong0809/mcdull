package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DeptVO;

import java.util.List;

/**
 * @author dqcer
 */
public interface IDeptService {


    List<DeptVO> list(DeptListDTO dto);

    boolean insert(DeptInsertDTO dto);

    boolean update(Integer id, DeptUpdateDTO dto);

    boolean delete(Integer id, ReasonDTO dto);

}
