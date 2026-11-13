package io.gitee.dqcer.mcdull.system.provider.web.service.administrator;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.DeptSaveDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminDeptEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminDeptInfoVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminDeptListTreeVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminDeptTreeVO;

import java.util.List;
import java.util.Map;

public interface IAdminDeptService extends IService<AdminDeptEntity> {


    AdminDeptTreeVO getDeptTree();

    AdminDeptListTreeVO getDeptListTree();

    Map<Integer, String> getDeptNameMap();

    Integer saveDept(DeptSaveDTO dto);

    Boolean deleteDept(List<Integer> idList);

    AdminDeptInfoVO get(Integer id);

    Boolean editDept(Integer id, DeptSaveDTO dto);

    void exportDept();
}
