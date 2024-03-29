package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.DeptEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DeptVO;

/**
 * 部门转换
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class DeptConvert {

    public static DeptVO convertToDeptVO(DeptEntity deptDO) {
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
        return deptVO;
    }

    public static DeptEntity convertToDeptDO(DeptVO deptVO) {
        DeptEntity deptDO = new DeptEntity();
        deptDO.setId(deptVO.getDeptId());
        deptDO.setParentId(deptVO.getParentId());
        deptDO.setAncestors(deptVO.getAncestors());
        deptDO.setName(deptVO.getDeptName());
        deptDO.setOrderNum(deptVO.getOrderNum());
        deptDO.setLeaderId(deptVO.getLeaderId());
        deptDO.setCreatedTime(deptVO.getCreatedTime());
        deptDO.setCreatedBy(deptVO.getCreatedBy());
        deptDO.setUpdatedTime(deptVO.getUpdatedTime());
        deptDO.setUpdatedBy(deptVO.getUpdatedBy());
        deptDO.setStatus(deptVO.getStatus());
        return deptDO;
    }




}
