package io.gitee.dqcer.mcdull.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.blaze.domain.entity.Approve;
import io.gitee.dqcer.mcdull.blaze.domain.enums.ApproveEnum;
import io.gitee.dqcer.mcdull.blaze.service.IApproveService;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ApproveVO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IUserManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApproveServiceImpl implements IApproveService {
    @Resource
    private IUserManager userManager;
    @Override
    public <T extends Approve> void approve(ApproveDTO dto, IRepository<T> baseRepository, Boolean existReferencedData) {
        Integer id = dto.getId();
        if (ObjUtil.isNotNull(id)) {
            T entity = baseRepository.getById(id);
            if (ObjUtil.isNotNull(entity)) {
                if (BooleanUtil.isTrue(existReferencedData)) {
                    if (ObjUtil.equal(entity.getApprove(), ApproveEnum.APPROVE.getCode())
                            && ObjUtil.equal(dto.getApprove(), ApproveEnum.REJECT.getCode())) {
                        throw new BusinessException("已存在关联数据，不能驳回");
                    }
                }
                entity.setApprove(dto.getApprove());
                entity.setApproveRemarks(dto.getRemark());
                baseRepository.updateById(entity);
            }
        }
    }

    @Override
    public void setApproveVO(List<? extends ApproveVO> list, List<? extends Approve> entityList) {
        if (CollUtil.isNotEmpty(list)) {
            for (ApproveVO vo : list) {
                Approve approve = entityList.stream()
                        .filter(entity -> entity.getId().equals(vo.getId()))
                        .findFirst()
                        .orElse(null);
                if (ObjUtil.isNotNull(approve)) {
                    vo.setApprove(approve.getApprove());
                    vo.setApproveRemarks(approve.getApproveRemarks());
                    vo.setResponsibleUserId(approve.getResponsibleUserId());
                    if (vo.getApprove() != null) {
                        vo.setApproveStr(IEnum.getTextByCode(ApproveEnum.class, vo.getApprove()));
                    }
                }
            }
            Set<Integer> collect = list.stream().map(ApproveVO::getResponsibleUserId).collect(Collectors.toSet());
            Map<Integer, String> nameMap = userManager.getNameMap(new ArrayList<>(collect));
            for (ApproveVO vo : list) {
                Integer responsibleUserId = vo.getResponsibleUserId();
                if (ObjUtil.isNotNull(responsibleUserId)) {
                    vo.setResponsibleUserIdStr(nameMap.get(responsibleUserId));
                }
            }
        }
    }
}
