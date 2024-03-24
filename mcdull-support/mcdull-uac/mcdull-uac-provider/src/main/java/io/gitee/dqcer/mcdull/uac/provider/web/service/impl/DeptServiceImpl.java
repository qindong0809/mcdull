package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DeptDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DeptVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDeptRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dqcer
 */
@Service
public class DeptServiceImpl extends BasicServiceImpl<IDeptRepository>  implements IDeptService {

    @Override
    public List<DeptVO> list(DeptListDTO dto) {
        List<DeptVO> list = new ArrayList<>();
        List<DeptDO> deptList = baseRepository.list();
        if (CollUtil.isNotEmpty(deptList)) {
            for (DeptDO dept : deptList) {
                DeptVO vo = this.convertToVO(dept);
                list.add(vo);
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insert(DeptInsertDTO dto) {
        Integer parentId = dto.getParentId();
        List<DeptDO> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream().anyMatch(i -> i.getName().equals(dto.getName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        DeptDO menu = this.convertToEntity(dto);
        return baseRepository.save(menu);
    }

    private DeptDO convertToEntity(DeptInsertDTO dto) {
        DeptDO deptDO = new DeptDO();
        deptDO.setName(dto.getName());
        deptDO.setParentId(dto.getParentId());
        deptDO.setSort(dto.getSort());
        deptDO.setPhone(dto.getPhone());
        deptDO.setPrincipal(dto.getPrincipal());
        deptDO.setEmail(dto.getEmail());
        deptDO.setType(dto.getType());
        deptDO.setRemark(dto.getRemark());
        deptDO.setInactive(this.getInactive(dto.getStatus()));
        return deptDO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Integer id, DeptUpdateDTO dto) {
        Integer parentId = dto.getParentId();
        List<DeptDO> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream()
                    .anyMatch(i -> (!i.getId().equals(id)) && i.getName().equals(dto.getName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        DeptDO menu = this.convertToEntity(dto);
        menu.setId(id);
        return baseRepository.updateById(menu);
    }

    private DeptDO convertToEntity(DeptUpdateDTO dto) {
        DeptDO deptDO = new DeptDO();
        deptDO.setName(dto.getName());
        deptDO.setParentId(dto.getParentId());
        deptDO.setSort(dto.getSort());
        deptDO.setPhone(dto.getPhone());
        deptDO.setPrincipal(dto.getPrincipal());
        deptDO.setEmail(dto.getEmail());
        deptDO.setType(dto.getType());
        deptDO.setRemark(dto.getRemark());
        deptDO.setInactive(this.getInactive(dto.getStatus()));
        return deptDO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer id, ReasonDTO dto) {
        return baseRepository.delete(id, dto.getReason());
    }

    private DeptVO convertToVO(DeptDO dept) {
        DeptVO deptVO = new DeptVO();
        deptVO.setId(dept.getId());
        deptVO.setStatus(this.getStatus(dept.getInactive()));
        deptVO.setCreateTime(dept.getCreatedTime());
        deptVO.setName(dept.getName());
        deptVO.setParentId(dept.getParentId());
        deptVO.setSort(dept.getSort());
        deptVO.setPhone(dept.getPhone());
        deptVO.setPrincipal(dept.getPrincipal());
        deptVO.setEmail(dept.getEmail());
        deptVO.setType(dept.getType());
        deptVO.setRemark(dept.getRemark());
        return deptVO;
    }

    private Integer getStatus(Boolean inactive) {
        return BooleanUtil.toInteger(!inactive);
    }

    private Boolean getInactive(Integer status) {
        return !BooleanUtil.toBooleanObject(Convert.toStr(status));
    }


}
