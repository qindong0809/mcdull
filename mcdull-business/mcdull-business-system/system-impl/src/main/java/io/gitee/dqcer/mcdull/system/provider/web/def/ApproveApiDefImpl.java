package io.gitee.dqcer.mcdull.system.provider.web.def;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.system.facade.model.dto.ApproveDeptQueryDTO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ApproveDeptVO;
import io.gitee.dqcer.mcdull.system.facade.service.ApproveApi;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDepartmentService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 审批人部门信息实现：同时作为本地 {@code @Primary} bean 与 HTTP 服务端点。
 */
@Primary
@RestController
public class ApproveApiDefImpl implements ApproveApi {

    @Resource
    private IDepartmentService departmentService;
    @Resource
    private IUserService userService;

    @Override
    public Map<Integer, ApproveDeptVO> deptInfo(ApproveDeptQueryDTO dto) {
        Map<Integer, ApproveDeptVO> result = new HashMap<>();
        List<Integer> responsibleUserIdList = dto.getResponsibleUserIdList();
        if (CollUtil.isEmpty(responsibleUserIdList)) {
            return result;
        }
        List<DepartmentEntity> departments = departmentService.list();
        Set<Integer> userIdSet = new HashSet<>(responsibleUserIdList);
        if (ObjUtil.isNotNull(dto.getUserId())) {
            userIdSet.add(dto.getUserId());
        }
        Map<Integer, UserEntity> userMap = userService.getEntityMap(new ArrayList<>(userIdSet));

        UserEntity currentUser = userMap.get(dto.getUserId());
        Integer currentRootDepartmentId = currentUser == null ? null : getRootDepartmentId(departments, currentUser.getDepartmentId());

        for (Integer responsibleUserId : responsibleUserIdList) {
            UserEntity responsibleUser = userMap.get(responsibleUserId);
            if (responsibleUser == null) {
                continue;
            }
            ApproveDeptVO vo = new ApproveDeptVO();
            Integer rootDepartmentId = getRootDepartmentId(departments, responsibleUser.getDepartmentId());
            vo.setIsSameDepartment(ObjUtil.equal(currentRootDepartmentId, rootDepartmentId));
            DepartmentEntity rootDepartment = departments.stream()
                    .filter(item -> ObjUtil.equal(item.getId(), rootDepartmentId))
                    .findFirst().orElse(null);
            if (rootDepartment != null) {
                vo.setResponsibleUserDepartmentId(rootDepartment.getId());
                vo.setResponsibleUserDepartmentName(rootDepartment.getName());
            }
            vo.setResponsibleUserPhone(responsibleUser.getPhone());
            result.put(responsibleUserId, vo);
        }
        return result;
    }

    private Integer getRootDepartmentId(List<DepartmentEntity> list, Integer departmentId) {
        if (ObjUtil.isNull(departmentId)) {
            return null;
        }
        for (DepartmentEntity entity : list) {
            if (ObjUtil.equal(entity.getId(), departmentId)) {
                if (ObjUtil.equal(entity.getParentId(), 1)) {
                    return entity.getId();
                }
                return getRootDepartmentId(list, entity.getParentId());
            }
        }
        return null;
    }
}
