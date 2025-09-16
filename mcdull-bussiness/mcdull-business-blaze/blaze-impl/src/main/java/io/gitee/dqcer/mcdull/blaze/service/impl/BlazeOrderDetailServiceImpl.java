package io.gitee.dqcer.mcdull.blaze.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.blaze.dao.repository.IBlazeOrderDetailRepository;
import io.gitee.dqcer.mcdull.blaze.domain.entity.BlazeOrderDetailEntity;
import io.gitee.dqcer.mcdull.blaze.domain.enums.ApproveEnum;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderDetailAddDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderDetailQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderDetailUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.BlazeOrderDetailListVO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.BlazeOrderDetailVO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.BlazeOrderVO;
import io.gitee.dqcer.mcdull.blaze.service.IApproveService;
import io.gitee.dqcer.mcdull.blaze.service.IBlazeOrderDetailService;
import io.gitee.dqcer.mcdull.blaze.service.IBlazeOrderService;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.web.enums.InactiveEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFileService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@Service
public class BlazeOrderDetailServiceImpl
        extends BasicServiceImpl<IBlazeOrderDetailRepository> implements IBlazeOrderDetailService {

    @Resource
    private IBlazeOrderService blazeOrderService;
    @Resource
    private IUserManager userManager;
    @Resource
    private ICommonManager commonManager;
    @Resource
    private IApproveService approveService;
    @Resource
    private IFileService fileService;

    @Override
    public PagedVO<BlazeOrderDetailVO> queryPage(BlazeOrderDetailQueryDTO dto) {
        List<BlazeOrderDetailVO> voList = new ArrayList<>();
        Page<BlazeOrderDetailEntity> entityPage = baseRepository.selectPage(dto);
        List<BlazeOrderDetailEntity> records = entityPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return PageUtil.toPage(voList, entityPage);
        }
        BlazeOrderQueryDTO orderQueryDTO = new BlazeOrderQueryDTO();
        PageUtil.setMaxPageSize(orderQueryDTO);
        PagedVO<BlazeOrderVO> page = blazeOrderService.queryPage(orderQueryDTO);
        List<BlazeOrderVO> orderVOList = CollUtil.defaultIfEmpty(page.getList(), new ArrayList<>());
        Map<Integer, BlazeOrderVO> orderMap = orderVOList.stream().collect(Collectors.toMap(BlazeOrderVO::getId, Function.identity()));
        Map<Integer, UserEntity> responsibleMap = userManager.getEntityMap(records.stream()
                .map(BlazeOrderDetailEntity::getResponsibleUserId)
                .filter(ObjUtil::isNotNull).collect(Collectors.toList()));
        Map<Integer, String> nameMap = userManager.getMap(records);
        for (BlazeOrderDetailEntity entity : records) {
            BlazeOrderDetailVO vo = this.convertToVO(entity);
            Integer blazeOrderId = vo.getBlazeOrderId();
            if (ObjUtil.isNotNull(orderMap.get(blazeOrderId))) {
                BlazeOrderVO orderVO = orderMap.get(blazeOrderId);
                vo.setOrderNo(orderVO.getOrderNo());
                vo.setTalentCertTitle(orderVO.getTalentCertName());
                vo.setCustomerCertTitle(orderVO.getCustomerCertName());
            }
            if (ObjUtil.isNotNull(responsibleMap.get(entity.getResponsibleUserId()))) {
                UserEntity responsibleUser = responsibleMap.get(entity.getResponsibleUserId());
                vo.setResponsibleUserName(responsibleUser.getActualName());
            }
            if (ObjUtil.isNotNull(entity.getInactive())) {
                vo.setInactiveStr(IEnum.getTextByCode(InactiveEnum.class, entity.getInactive()));
            }
            if (ObjUtil.isNotNull(entity.getCreatedBy())) {
                vo.setCreatedByStr(nameMap.get(entity.getCreatedBy()));
            }
            if (ObjUtil.isNotNull(entity.getUpdatedBy())) {
                vo.setUpdatedByStr(nameMap.get(entity.getUpdatedBy()));
            }
            vo.setOperationTimeStr(vo.getOperationTime());
            voList.add(vo);
        }
        approveService.setApproveVO(voList, records);
        commonManager.setFileVO(voList, BlazeOrderDetailEntity.class);
        commonManager.setDepartment(voList, UserContextHolder.userId());
        return PageUtil.toPage(voList, entityPage);
    }

    private BlazeOrderDetailVO convertToVO(BlazeOrderDetailEntity entity) {
        BlazeOrderDetailVO vo = new BlazeOrderDetailVO();
        vo.setId(entity.getId());
        vo.setBlazeOrderId(entity.getBlazeOrderId());
        vo.setIsTalent(entity.getIsTalent());
        vo.setResponsibleUserId(entity.getResponsibleUserId());
        vo.setOperationTime(entity.getOperationTime());
        vo.setPrice(entity.getPrice().toString());
        vo.setRemarks(entity.getRemarks());
        vo.setCreatedBy(entity.getCreatedBy());
        vo.setCreatedTime(entity.getCreatedTime());
        vo.setUpdatedBy(entity.getUpdatedBy());
        vo.setUpdatedTime(entity.getUpdatedTime());
        vo.setInactive(entity.getInactive());
        vo.setInactiveStr(IEnum.getTextByCode(InactiveEnum.class, entity.getInactive()));
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean exportData(BlazeOrderDetailQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitleList(dto.getIsTalent()));
        return true;
    }

    private List<Pair<String, Func1<BlazeOrderDetailVO, ?>>> getTitleList(Boolean isTalent) {
        List<Pair<String, Func1<BlazeOrderDetailVO, ?>>> list = new ArrayList<>();
        list.add(Pair.of("订单编号", BlazeOrderDetailVO::getOrderNo));
        if (ObjUtil.isNotNull(isTalent) && isTalent) {
            list.add(Pair.of("所属人才证书", BlazeOrderDetailVO::getTalentCertTitle));
        } else {
            list.add(Pair.of("所属企业证书需求", BlazeOrderDetailVO::getCustomerCertTitle));
        }
        list.add(Pair.of("本次金额", BlazeOrderDetailVO::getPrice));
        list.add(Pair.of("负责人", BlazeOrderDetailVO::getResponsibleUserName));
        list.add(Pair.of("操作时间", BlazeOrderDetailVO::getOperationTimeStr));
        list.add(Pair.of("备注", BlazeOrderDetailVO::getRemarks));
        list.add(Pair.of("创建人", BlazeOrderDetailVO::getCreatedByStr));
        list.add(Pair.of("创建时间", BlazeOrderDetailVO::getCreatedTime));
        list.add(Pair.of("更新人", BlazeOrderDetailVO::getUpdatedByStr));
        list.add(Pair.of("更新时间", BlazeOrderDetailVO::getUpdatedTime));
        list.add(Pair.of("状态", BlazeOrderDetailVO::getInactiveStr));
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(BlazeOrderDetailAddDTO dto, List<MultipartFile> fileList) {
        BlazeOrderDetailEntity entity = this.convertToEntity(dto);
        entity.setApprove(ApproveEnum.NOT_APPROVE.getCode());
        baseRepository.save(entity);
        fileService.batchFileUpload(fileList, entity.getId(), BlazeOrderDetailEntity.class, null);
    }

    private BlazeOrderDetailEntity convertToEntity(BlazeOrderDetailAddDTO dto) {
        BlazeOrderDetailEntity entity = new BlazeOrderDetailEntity();
        entity.setBlazeOrderId(dto.getBlazeOrderId());
        entity.setIsTalent(dto.getIsTalent());
        entity.setResponsibleUserId(dto.getResponsibleUserId());
        entity.setOperationTime(dto.getOperationTime());
        entity.setPrice(dto.getPrice());
        entity.setRemarks(dto.getRemarks());
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(BlazeOrderDetailUpdateDTO dto, List<MultipartFile> fileList) {
        Integer id = dto.getId();
        BlazeOrderDetailEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.settingUpdateValue(dto, entity);
        baseRepository.updateById(entity);
        fileService.batchFileUpload(fileList, id, BlazeOrderDetailEntity.class, dto.getDeleteFileIdList());
    }

    private void settingUpdateValue(BlazeOrderDetailUpdateDTO dto, BlazeOrderDetailEntity entity) {
        entity.setResponsibleUserId(dto.getResponsibleUserId());
        entity.setOperationTime(dto.getOperationTime());
        entity.setPrice(dto.getPrice());
        entity.setRemarks(dto.getRemarks());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        BlazeOrderDetailEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        baseRepository.removeById(id);
        fileService.remove(id, BlazeOrderDetailEntity.class);
    }

    @Override
    public List<BlazeOrderDetailListVO> getOderListByTalent() {
        BlazeOrderQueryDTO dto = new BlazeOrderQueryDTO();
        dto.setApprove(ApproveEnum.APPROVE.getCode());
        PageUtil.setMaxPageSize(dto);
        PagedVO<BlazeOrderVO> page = blazeOrderService.queryPage(dto);
        if (CollUtil.isNotEmpty(page.getList())) {
            List<BlazeOrderDetailListVO> voList = new ArrayList<>();
            List<BlazeOrderVO> list = page.getList();
            for (BlazeOrderVO blazeOrderVO : list) {
                BlazeOrderDetailListVO vo = new BlazeOrderDetailListVO();
                vo.setBlazeOrderId(blazeOrderVO.getId());
                vo.setOrderNo(blazeOrderVO.getOrderNo());
                vo.setTalentCertTitle(blazeOrderVO.getTalentCertName());
                vo.setCustomerCertTitle(blazeOrderVO.getCustomerCertName());
                voList.add(vo);
            }
            return voList;
        }
        return List.of();
    }

    @Override
    public List<BlazeOrderDetailListVO> getOderListByCustomer() {
        BlazeOrderQueryDTO dto = new BlazeOrderQueryDTO();
        PageUtil.setMaxPageSize(dto);
        // todo 状态过滤，打款完成过滤
        PagedVO<BlazeOrderVO> page = blazeOrderService.queryPage(dto);
        if (CollUtil.isNotEmpty(page.getList())) {
            List<BlazeOrderDetailListVO> voList = new ArrayList<>();
            List<BlazeOrderVO> list = page.getList();
            for (BlazeOrderVO blazeOrderVO : list) {
                BlazeOrderDetailListVO vo = new BlazeOrderDetailListVO();
                vo.setBlazeOrderId(blazeOrderVO.getId());
                vo.setOrderNo(blazeOrderVO.getOrderNo());
                vo.setTalentCertTitle(blazeOrderVO.getTalentCertName());
                vo.setCustomerCertTitle(blazeOrderVO.getCustomerCertName());
                voList.add(vo);
            }
            return voList;
        }
        return List.of();
    }

    @Override
    public boolean isTalent(Integer id) {
        BlazeOrderDetailEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        return entity.getIsTalent();
    }

    @Override
    public List<LabelValueVO<Integer, String>> getResponsibleList() {
        return userManager.getResponsibleList();
    }

    @Override
    public BlazeOrderDetailEntity getByOrderId(Integer id) {
        List<BlazeOrderDetailEntity> list = baseRepository.getByOrderId(ListUtil.of(id));
        return CollUtil.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public List<BlazeOrderDetailEntity> getByOrderId(List<Integer> orderIdList) {
        return baseRepository.getByOrderId(orderIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approve(ApproveDTO dto) {
        approveService.approve(dto, baseRepository, null);
    }
}
