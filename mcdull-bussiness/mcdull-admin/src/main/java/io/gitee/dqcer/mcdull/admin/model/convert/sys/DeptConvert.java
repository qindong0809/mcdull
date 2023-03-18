package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.DeptDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DeptVO;

/**
 * 部门转换
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class DeptConvert {

    public static DeptVO convertToDeptVO(DeptDO deptDO) {
        DeptVO deptVO = new DeptVO();
        deptVO.setDeptId(deptDO.getId());
        deptVO.setParentId(deptDO.getParentId());
        deptVO.setAncestors(deptDO.getAncestors());
        deptVO.setDeptName(deptDO.getName());
        deptVO.setOrderNum(deptDO.getOrderNum());
        deptVO.setLeaderId(deptDO.getLeaderId());
        deptVO.setCreatedTime(deptDO.getCreatedTime());
        deptVO.setCreatedBy(deptDO.getCreatedBy());
        deptVO.setUpdatedTime(deptDO.getUpdatedTime());
        deptVO.setUpdatedBy(deptDO.getUpdatedBy());
        deptVO.setStatus(deptDO.getStatus());
        deptVO.setDelFlag(deptDO.getDelFlag());
        return deptVO;
    }


}
