package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.DeptConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DeptLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DeptDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DeptVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IDeptRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDeptService;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * sys dict服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class DeptServiceImpl implements IDeptService {

    @Resource
    private IDeptRepository deptRepository;


    @Override
    public Result<List<DeptVO>> list(DeptLiteDTO dto) {
        List<DeptVO> voList = new ArrayList<>();
        List<DeptDO> list = deptRepository.list(dto.getDeptName(), dto.getStatus());
        for (DeptDO deptDO : list) {
            DeptVO deptVO = DeptConvert.convertToDeptVO(deptDO);
            voList.add(deptVO);
        }
        return Result.ok(voList);
    }

    @Override
    public Result<List<DeptVO>> excludeChild(Long deptId) {
        List<DeptVO> voList = new ArrayList<>();
        List<DeptDO> list = deptRepository.list();
        for (DeptDO deptDO : list) {
            DeptVO deptVO = DeptConvert.convertToDeptVO(deptDO);
            voList.add(deptVO);
        }
        voList.removeIf( d -> d.getDeptId().equals(deptId) || ArrayUtil.contains(StrUtil.splitToArray(d.getAncestors(), ","), deptId));
        return Result.ok(voList);
    }

    @Override
    public Result<DeptVO> detail(Long deptId) {
        DeptDO dept = deptRepository.getById(deptId);
        return Result.ok(DeptConvert.convertToDeptVO(dept));
    }
}
