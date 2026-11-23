package io.gitee.dqcer.mcdull.blaze.web.service.impl;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.blaze.model.entity.BlazeOrderDetailEntity;
import io.gitee.dqcer.mcdull.blaze.model.enums.ApproveEnum;
import io.gitee.dqcer.mcdull.blaze.model.dto.BlazeOrderDetailAddDTO;
import io.gitee.dqcer.mcdull.blaze.model.dto.BlazeOrderDetailQueryDTO;
import io.gitee.dqcer.mcdull.blaze.model.dto.BlazeOrderDetailUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.model.dto.BlazeOrderQueryDTO;
import io.gitee.dqcer.mcdull.blaze.model.vo.BlazeOrderDetailListVO;
import io.gitee.dqcer.mcdull.blaze.model.vo.BlazeOrderDetailVO;
import io.gitee.dqcer.mcdull.blaze.model.vo.BlazeOrderVO;
import io.gitee.dqcer.mcdull.blaze.web.manager.IBlazeOrderDetailRepository;
import io.gitee.dqcer.mcdull.blaze.web.service.IApproveService;
import io.gitee.dqcer.mcdull.blaze.web.service.IBlazeOrderDetailService;
import io.gitee.dqcer.mcdull.blaze.web.service.IBlazeOrderService;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.mysql.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.common.CustomMultipartFile;
import io.gitee.dqcer.mcdull.common.excel.ExcelExportUtil;
import io.gitee.dqcer.mcdull.common.user.UserRefUtil;
import io.gitee.dqcer.mcdull.blaze.util.TableNameUtil;
import io.gitee.dqcer.mcdull.framework.web.enums.InactiveEnum;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.system.facade.model.dto.ApproveDeptQueryDTO;
import io.gitee.dqcer.mcdull.system.facade.model.dto.FileBizDTO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ApproveDeptVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ApproveVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ExportContextVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.FileSimpleVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.IFileVO;
import io.gitee.dqcer.mcdull.system.facade.model.dto.UserDTO;
import io.gitee.dqcer.mcdull.system.facade.service.ApproveApi;
import io.gitee.dqcer.mcdull.system.facade.service.ExportApi;
import io.gitee.dqcer.mcdull.system.facade.service.FileRemoteApi;
import io.gitee.dqcer.mcdull.system.facade.service.def.UserApiDef;
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
    private UserApiDef sysUserFacade;
    @Resource
    private IApproveService approveService;
    @Resource
    private ApproveApi approveApi;
    @Resource
    private ExportApi exportApi;
    @Resource
    private FileRemoteApi fileRemoteApi;

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
        Map<Integer, UserDTO> responsibleMap = sysUserFacade.getEntityMap(records.stream()
                .map(BlazeOrderDetailEntity::getResponsibleUserId)
                .filter(ObjUtil::isNotNull).collect(Collectors.toList()));
        Map<Integer, String> nameMap = sysUserFacade.getNameMap(UserRefUtil.collectUserIds(records));
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
                UserDTO responsibleUser = responsibleMap.get(entity.getResponsibleUserId());
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
        fillFileVO(voList);
        fillDepartment(voList);
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

    private void fillFileVO(List<BlazeOrderDetailVO> voList) {
        if (CollUtil.isEmpty(voList)) {
            return;
        }
        FileBizDTO dto = new FileBizDTO();
        dto.setBizType(TableNameUtil.tableName(BlazeOrderDetailEntity.class));
        dto.setBizIdList(voList.stream().map(IFileVO::getId).collect(Collectors.toList()));
        Map<Integer, List<FileSimpleVO>> fileMap = fileRemoteApi.listFiles(dto);
        for (BlazeOrderDetailVO vo : voList) {
            vo.setFileList(fileMap.get(vo.getId()));
        }
    }

    private void fillDepartment(List<BlazeOrderDetailVO> voList) {
        if (CollUtil.isEmpty(voList)) {
            return;
        }
        ApproveDeptQueryDTO dto = new ApproveDeptQueryDTO();
        dto.setUserId(UserContextHolder.userId());
        dto.setResponsibleUserIdList(voList.stream()
                .map(ApproveVO::getResponsibleUserId)
                .filter(ObjUtil::isNotNull)
                .distinct()
                .collect(Collectors.toList()));
        Map<Integer, ApproveDeptVO> deptMap = approveApi.deptInfo(dto);
        for (BlazeOrderDetailVO vo : voList) {
            ApproveDeptVO dept = deptMap.get(vo.getResponsibleUserId());
            if (dept != null) {
                vo.setIsSameDepartment(dept.getIsSameDepartment());
                vo.setResponsibleUserDepartmentId(dept.getResponsibleUserDepartmentId());
                vo.setResponsibleUserDepartmentName(dept.getResponsibleUserDepartmentName());
                vo.setResponsibleUserPhone(dept.getResponsibleUserPhone());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean exportData(BlazeOrderDetailQueryDTO dto) {
        ExportContextVO context = exportApi.context();
        List<BlazeOrderDetailVO> list = this.queryPage(dto).getList();
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        ExcelExportUtil.write(outputStream, list, StrUtil.EMPTY,
                context.getOperatorName(), context.getDateTimeStr(), this.getTitleList(dto.getIsTalent()));
        byte[] bytes = outputStream.toByteArray();
        try (var responseStream = ServletUtil.getResponse().getOutputStream()) {
            responseStream.write(bytes);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (Boolean.TRUE.equals(context.getAutoStorage())) {
            fileRemoteApi.uploadToSystemFolder(new CustomMultipartFile(context.getMenuName() + ".xlsx", bytes));
        }
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
        this.saveFiles(entity.getId(), fileList, null);
    }

    private void saveFiles(Integer bizId, List<MultipartFile> fileList, List<Integer> deleteFileIdList) {
        FileBizDTO dto = new FileBizDTO();
        dto.setBizId(bizId);
        dto.setBizType(TableNameUtil.tableName(BlazeOrderDetailEntity.class));
        dto.setDeleteFileIdList(deleteFileIdList);
        fileRemoteApi.batchUpload(dto, fileList);
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
        this.saveFiles(id, fileList, dto.getDeleteFileIdList());
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
        FileBizDTO dto = new FileBizDTO();
        dto.setBizId(id);
        dto.setBizType(TableNameUtil.tableName(BlazeOrderDetailEntity.class));
        fileRemoteApi.remove(dto);
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
        return sysUserFacade.getResponsibleList();
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
