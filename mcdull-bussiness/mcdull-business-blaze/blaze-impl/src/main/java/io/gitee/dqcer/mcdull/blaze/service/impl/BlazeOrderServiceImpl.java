package io.gitee.dqcer.mcdull.blaze.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.blaze.dao.repository.IBlazeOrderRepository;
import io.gitee.dqcer.mcdull.blaze.domain.entity.BlazeOrderDetailEntity;
import io.gitee.dqcer.mcdull.blaze.domain.entity.BlazeOrderEntity;
import io.gitee.dqcer.mcdull.blaze.domain.entity.CertificateRequirementsEntity;
import io.gitee.dqcer.mcdull.blaze.domain.entity.TalentCertificateEntity;
import io.gitee.dqcer.mcdull.blaze.domain.enums.ApproveEnum;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderAddDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderSearchDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.BlazeOrderUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.BlazeOrderVO;
import io.gitee.dqcer.mcdull.blaze.service.*;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.web.enums.InactiveEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DepartmentInfoVO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDepartmentService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFileService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISerialNumberService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单合同 Service
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@Service
public class BlazeOrderServiceImpl
        extends BasicServiceImpl<IBlazeOrderRepository> implements IBlazeOrderService {

    @Resource
    private ICertificateRequirementsService certificateRequirementsService;
    @Resource
    private ITalentCertificateService talentCertificateService;
    @Resource
    private ICommonManager commonManager;
    @Resource
    private IUserManager userManager;
    @Resource
    private ISerialNumberService serialNumberService;
    @Resource
    private IBlazeOrderDetailService blazeOrderDetailService;
    @Resource
    private IApproveService approveService;
    @Resource
    private IFileService fileService;
    @Resource
    private IDepartmentService departmentService;

    public PagedVO<BlazeOrderVO> queryPage(BlazeOrderQueryDTO dto) {
        List<BlazeOrderVO> voList = new ArrayList<>();
        List<BlazeOrderEntity> recordList = baseRepository.selectList(dto);
        if (CollUtil.isNotEmpty(recordList)) {
            Map<Integer, String> nameMap = userManager.getMap(recordList);
            List<Integer> orderIdList = recordList.stream().map(IdEntity::getId).toList();
            List<BlazeOrderDetailEntity> orderDetailList = blazeOrderDetailService.getByOrderId(orderIdList);
            List<LabelValueVO<Integer, String>> list = CollUtil.emptyIfNull(certificateRequirementsService.all(false));
            Map<Integer, String> map = list.stream().collect(Collectors.toMap(LabelValueVO::getValue, LabelValueVO::getLabel));
            List<LabelValueVO<Integer, String>> talentList = CollUtil.emptyIfNull(talentCertificateService.list(null, false));
            Map<Integer, String> talentMap = talentList.stream().collect(Collectors.toMap(LabelValueVO::getValue, LabelValueVO::getLabel));
            Map<Integer, CertificateRequirementsEntity> custMap = certificateRequirementsService.map(recordList.stream().map(BlazeOrderEntity::getCustomerCertId).collect(Collectors.toSet()));
            Map<Integer, TalentCertificateEntity> talentEntityMap = talentCertificateService.map(recordList.stream().map(BlazeOrderEntity::getTalentCertId).collect(Collectors.toSet()));
            for (BlazeOrderEntity entity : recordList) {
                BlazeOrderVO vo = this.convertToVO(entity);
                vo.setOrderNo(entity.getOrderNo());
                vo.setCustomerCertName(map.get(entity.getCustomerCertId()));
                vo.setTalentCertName(talentMap.get(entity.getTalentCertId()));
                CertificateRequirementsEntity requirementsEntity = custMap.get(entity.getCustomerCertId());
                if (ObjUtil.isNotNull(requirementsEntity)) {
                    vo.setEnterpriseCollection(requirementsEntity.getPositionContractPrice().toString());
                    vo.setCustomerResponsibleUserId(requirementsEntity.getResponsibleUserId());
                }
                TalentCertificateEntity talentCertificateEntity = talentEntityMap.get(entity.getTalentCertId());
                if (ObjUtil.isNotNull(talentCertificateEntity)) {
                    vo.setTalentPayment(talentCertificateEntity.getPositionContractPrice().toString());
                    vo.setTalentResponsibleUserId(talentCertificateEntity.getResponsibleUserId());
                }
                if (ObjUtil.isNotNull(requirementsEntity) && ObjUtil.isNotNull(talentCertificateEntity)) {
                    BigDecimal actualPositionPrice = requirementsEntity.getActualPositionPrice();
                    if (ObjUtil.isNotNull(actualPositionPrice)) {
                        // 单本价格
                        BigDecimal talentCertificateEntityActualPositionPrice = talentCertificateEntity.getActualPositionPrice();
                        if (ObjUtil.isNotNull(talentCertificateEntityActualPositionPrice)) {
                            String performance = NumberUtil.sub(actualPositionPrice, talentCertificateEntityActualPositionPrice).toString();
                            vo.setPerformance(performance);
                        }
                    }
                }

                if (ObjUtil.isNotNull(entity.getContractTime())) {
                    vo.setContractTimeStr(entity.getContractTime());
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
                BigDecimal talent = orderDetailList.stream()
                        .filter(i-> i.getBlazeOrderId().equals(entity.getId()) && BooleanUtil.isTrue(i.getIsTalent()) && ObjUtil.equals(i.getApprove(), ApproveEnum.APPROVE.getCode()))
                        .map(BlazeOrderDetailEntity::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                vo.setNowTalentPayment(talent.toString());
                BigDecimal customer = orderDetailList.stream()
                        .filter(i-> i.getBlazeOrderId().equals(entity.getId()) && BooleanUtil.isFalse(i.getIsTalent()) && ObjUtil.equals(i.getApprove(), ApproveEnum.APPROVE.getCode()))
                        .map(BlazeOrderDetailEntity::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                vo.setNowEnterpriseCollection(customer.toString());
                voList.add(vo);
            }
            Set<Integer> collect = voList.stream().map(BlazeOrderVO::getTalentResponsibleUserId).collect(Collectors.toSet());
            collect.addAll(voList.stream().map(BlazeOrderVO::getCustomerResponsibleUserId).collect(Collectors.toSet()));
            Map<Integer, UserEntity> userMap = userManager.getEntityMap(new ArrayList<>(collect));
            for (BlazeOrderVO orderVO : voList) {
                if (MapUtil.isNotEmpty(userMap)) {
                    UserEntity user = userMap.get(orderVO.getTalentResponsibleUserId());
                    if (ObjUtil.isNotNull(user)) {
                        orderVO.setTalentResponsibleUserPhone(user.getPhone());
                        orderVO.setTalentResponsibleUserIdStr(user.getActualName());
                        orderVO.setTalentResponsibleDepartmentId(user.getDepartmentId());
                    }
                    UserEntity customerUser =  userMap.get(orderVO.getCustomerResponsibleUserId());
                    if (ObjUtil.isNotNull(customerUser)) {
                        orderVO.setCustomerResponsibleUserPhone(customerUser.getPhone());
                        orderVO.setCustomerResponsibleUserIdStr(customerUser.getActualName());
                        orderVO.setCustomerResponsibleDepartmentId(customerUser.getDepartmentId());
                    }
                }
            }
            Set<Integer> collect1 = voList.stream().map(BlazeOrderVO::getTalentResponsibleDepartmentId).collect(Collectors.toSet());
            collect1.addAll(voList.stream().map(BlazeOrderVO::getCustomerResponsibleDepartmentId).collect(Collectors.toSet()));
            List<DepartmentInfoVO> all = departmentService.getAll();
            Map<Integer, String> departmentMap = all.stream().collect(Collectors.toMap(DepartmentInfoVO::getDepartmentId, DepartmentInfoVO::getName));
            for (BlazeOrderVO orderVO : voList) {
                if (MapUtil.isNotEmpty(departmentMap)) {
                    orderVO.setTalentResponsibleDepartment(departmentMap.get(orderVO.getTalentResponsibleDepartmentId()));
                    orderVO.setCustomerResponsibleDepartment(departmentMap.get(orderVO.getCustomerResponsibleDepartmentId()));
                }
            }
            approveService.setApproveVO(voList, recordList);
            commonManager.setFileVO(voList, BlazeOrderEntity.class);
            commonManager.setDepartment(voList, UserContextHolder.userId());
        }
        List<Integer> userIdList = CollUtil.defaultIfEmpty(dto.getResponsibleUserIdList(), new ArrayList<>());
        if (CollUtil.isNotEmpty(userIdList)) {
            voList = voList.stream()
                    .filter(i -> userIdList.contains(i.getResponsibleUserId())
                            || userIdList.contains(i.getTalentResponsibleUserId())
                            || userIdList.contains(i.getCustomerResponsibleUserId())).collect(Collectors.toList());
        }
        return PageUtil.of(voList, dto);
    }

    @Override
    public boolean existByTalentCertificateId(List<Integer> talentCertificateIdList) {
        if (CollUtil.isNotEmpty(talentCertificateIdList)) {
            LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
            query.in(BlazeOrderEntity::getTalentCertId, talentCertificateIdList);
            return baseRepository.exists(query);
        }
        return false;
    }

    @Override
    public boolean existByCertificateRequirementsIdList(List<Integer> certificateRequirementsIdList) {
        if (CollUtil.isNotEmpty(certificateRequirementsIdList)) {
            LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
            query.in(BlazeOrderEntity::getCustomerCertId, certificateRequirementsIdList);
            return baseRepository.exists(query);
        }
        return false;
    }

    @Override
    public boolean exportData(BlazeOrderQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitleList());
        return true;
    }

    @Override
    public Map<Integer, Boolean> getMapByTalentCertId(Set<Integer> collect) {
        if (CollUtil.isNotEmpty(collect)) {
            LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
            query.in(BlazeOrderEntity::getTalentCertId, collect);
            List<BlazeOrderEntity> list = baseRepository.list(query);
            if (CollUtil.isNotEmpty(list)) {
                Map<Integer, Boolean> map = new HashMap<>();
                for (Integer id : collect) {
                    boolean exist = list.stream().anyMatch(entity -> entity.getTalentCertId().equals(id));
                    map.put(id, exist);
                }
                return map;
            }
        }
        return Map.of();
    }

    @Override
    public Map<Integer, Boolean> getMapByCustomerCertId(Collection<Integer> collect) {
        if (CollUtil.isNotEmpty(collect)) {
            LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
            query.in(BlazeOrderEntity::getTalentCertId, collect);
            List<BlazeOrderEntity> list = baseRepository.list(query);
            if (CollUtil.isNotEmpty(list)) {
                Map<Integer, Boolean> map = new HashMap<>();
                for (Integer id : collect) {
                    boolean exist = list.stream().anyMatch(entity -> entity.getCustomerCertId().equals(id));
                    map.put(id, exist);
                }
                return map;
            }
        }
        return Map.of();
    }

    @Override
    public List<BlazeOrderEntity> list() {
        return baseRepository.list();
    }

    @Override
    public List<LabelValueVO<Integer, String>> getCustomerCertListByOrderId(Integer orderId) {
        List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
        if (ObjUtil.isNotNull(orderId)) {
            BlazeOrderEntity blazeOrder = baseRepository.getById(orderId);
            if (ObjUtil.isNotNull(blazeOrder)) {
                Integer customerCertId = blazeOrder.getCustomerCertId();
                Map<Integer, CertificateRequirementsEntity> requirementsEntityMap = certificateRequirementsService.map(Set.of(customerCertId));
                CertificateRequirementsEntity entity = requirementsEntityMap.get(customerCertId);
                if (ObjUtil.isNotNull(entity)) {
                    LabelValueVO<Integer, String> vo = new LabelValueVO<>(entity.getId(), entity.getPositionTitle());
                    voList.add(vo);
                }
            }
        }
        List<LabelValueVO<Integer, String>> list = certificateRequirementsService.okList();
        if (CollUtil.isNotEmpty(list)) {
            for (LabelValueVO<Integer, String> vo : list) {
                if (!voList.contains(vo)) {
                    voList.add(vo);
                }
            }
        }
        return voList;
    }

    @Override
    public List<LabelValueVO<Integer, String>> getTalentCertListByOrderId(Integer customerCertId, Integer orderId) {
        List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
        if (ObjUtil.isNotNull(customerCertId)) {
            if (ObjUtil.isNotNull(orderId)) {
                BlazeOrderEntity blazeOrder = baseRepository.getById(orderId);
                if (ObjUtil.isNotNull(blazeOrder)) {
                    Integer talentCertId = blazeOrder.getTalentCertId();
                    Map<Integer, TalentCertificateEntity> entityMap = talentCertificateService.map(Set.of(talentCertId));
                    if (ObjUtil.isNotNull(entityMap)) {
                        TalentCertificateEntity entity = entityMap.get(talentCertId);
                        if (ObjUtil.isNotNull(entity)) {
                            LabelValueVO<Integer, String> vo = new LabelValueVO<>(entity.getId(), entity.getPositionTitle());
                            voList.add(vo);
                        }
                    }
                }
            }
            List<LabelValueVO<Integer, String>> list = talentCertificateService.list(customerCertId, true);
            if (CollUtil.isNotEmpty(list)) {
                for (LabelValueVO<Integer, String> vo : list) {
                    if (!voList.contains(vo)) {
                        voList.add(vo);
                    }
                }
            }
        }
        return voList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approve(ApproveDTO dto) {
        BlazeOrderDetailEntity entity = blazeOrderDetailService.getByOrderId(dto.getId());
        approveService.approve(dto, baseRepository, ObjUtil.isNotNull(entity));
    }

    @Override
    public List<LabelValueVO<Integer, String>> getTalentCertList(BlazeOrderSearchDTO searchDTO) {
        List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
        Integer customerCertId = searchDTO.getCustomerCertId();
        Integer orderId = searchDTO.getOrderId();
        // 编辑回显
        if (ObjUtil.isNotNull(orderId)) {
            BlazeOrderEntity blazeOrder = baseRepository.getById(orderId);
            if (ObjUtil.isNotNull(blazeOrder)) {
                Integer talentCertId = blazeOrder.getTalentCertId();
                Map<Integer, TalentCertificateEntity> entityMap = talentCertificateService.map(Set.of(talentCertId));
                if (ObjUtil.isNotNull(entityMap)) {
                    TalentCertificateEntity entity = entityMap.get(talentCertId);
                    if (ObjUtil.isNotNull(entity)) {
                        LabelValueVO<Integer, String> vo = new LabelValueVO<>(entity.getId(), entity.getPositionTitle());
                        voList.add(vo);
                    }
                }
            }
        }
        List<LabelValueVO<Integer, String>> list = talentCertificateService.getList(customerCertId);
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, Boolean> existMap = this.getMapByTalentCertId(list.stream().map(LabelValueVO::getValue).collect(Collectors.toSet()));
            for (LabelValueVO<Integer, String> vo : list) {
                if (!voList.contains(vo)) {
                    if (BooleanUtil.isFalse(existMap.getOrDefault(vo.getValue(), false))) {
                        voList.add(vo);
                    }
                }
            }
        }
        return voList;
    }

    @Override
    public List<LabelValueVO<Integer, String>> getCustomCertList(BlazeOrderSearchDTO pkDTO) {
        List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
        Integer orderId = pkDTO.getOrderId();
        Integer talentCertId = pkDTO.getTalentCertId();
        if (ObjUtil.isNotNull(orderId)) {
            BlazeOrderEntity blazeOrder = baseRepository.getById(orderId);
            if (ObjUtil.isNotNull(blazeOrder)) {
                Integer customerCertId = blazeOrder.getCustomerCertId();
                Map<Integer, CertificateRequirementsEntity> requirementsEntityMap = certificateRequirementsService.map(Set.of(customerCertId));
                CertificateRequirementsEntity entity = requirementsEntityMap.get(customerCertId);
                if (ObjUtil.isNotNull(entity)) {
                    LabelValueVO<Integer, String> vo = new LabelValueVO<>(entity.getId(), entity.getPositionTitle());
                    voList.add(vo);
                }
            }
        }
        List<LabelValueVO<Integer, String>> list = certificateRequirementsService.getList(talentCertId);
        if (CollUtil.isNotEmpty(list)) {
            List<BlazeOrderEntity> orderEntityList = CollUtil.defaultIfEmpty(this.list(), new ArrayList<>());
            for (LabelValueVO<Integer, String> vo : list) {
                if (!voList.contains(vo)) {
                    Integer currentCount = Convert.toInt(orderEntityList.stream().filter(item -> item.getCustomerCertId().equals(vo.getValue())).count(), 0);
                    CertificateRequirementsEntity entity = certificateRequirementsService.get(vo.getValue());
                    if (entity.getQuantity() > currentCount) {
                        voList.add(new LabelValueVO<>(vo.getValue(), entity.getPositionTitle()));
                    }
                }
            }
        }
        return voList;
    }

    @Override
    public BlazeOrderEntity getByCustomerCertId(Integer customerCertId) {
        LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
        query.eq(BlazeOrderEntity::getCustomerCertId, customerCertId);
        List<BlazeOrderEntity> list = baseRepository.list(query);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public BlazeOrderEntity getByTalentCertId(Integer talentCertId) {
        LambdaQueryWrapper<BlazeOrderEntity> query = Wrappers.lambdaQuery();
        query.eq(BlazeOrderEntity::getTalentCertId, talentCertId);
        List<BlazeOrderEntity> list = baseRepository.list(query);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    private List<Pair<String, Func1<BlazeOrderVO, ?>>> getTitleList() {
        List<Pair<String, Func1<BlazeOrderVO, ?>>> list = new ArrayList<>();
        list.add(Pair.of("订单编号", BlazeOrderVO::getOrderNo));
        list.add(Pair.of("人才归属", BlazeOrderVO::getTalentResponsibleDepartment));
        list.add(Pair.of("人才负责人", BlazeOrderVO::getTalentResponsibleUserIdStr));
        list.add(Pair.of("人才负责人电话", BlazeOrderVO::getTalentResponsibleUserPhone));
        list.add(Pair.of("所属人才", BlazeOrderVO::getTalentCertName));
        list.add(Pair.of("应人才打款", BlazeOrderVO::getTalentPayment));
        list.add(Pair.of("现人才打款", BlazeOrderVO::getNowTalentPayment));
        list.add(Pair.of("业绩", BlazeOrderVO::getPerformance));
        list.add(Pair.of("企业归属", BlazeOrderVO::getCustomerResponsibleDepartment));
        list.add(Pair.of("企业负责人", BlazeOrderVO::getCustomerResponsibleUserIdStr));
        list.add(Pair.of("企业负责人电话", BlazeOrderVO::getCustomerResponsibleUserPhone));
        list.add(Pair.of("所属企业", BlazeOrderVO::getCustomerCertName));
        list.add(Pair.of("应企业回款", BlazeOrderVO::getEnterpriseCollection));
        list.add(Pair.of("现企业回款", BlazeOrderVO::getNowEnterpriseCollection));
        list.add(Pair.of("合同日期", BlazeOrderVO::getContractTime));
        list.add(Pair.of("开始日期", BlazeOrderVO::getStartDate));
        list.add(Pair.of("结束日期", BlazeOrderVO::getEndDate));
        list.add(Pair.of("距今剩余", BlazeOrderVO::getDaysUntilDue));
        list.add(Pair.of("备注", BlazeOrderVO::getRemarks));
        return list;
    }

    private BlazeOrderVO convertToVO(BlazeOrderEntity item){
        BlazeOrderVO vo = new BlazeOrderVO();
        vo.setId(item.getId());
        vo.setTalentCertId(item.getTalentCertId());
        vo.setCustomerCertId(item.getCustomerCertId());
        vo.setContractTime(item.getContractTime());
        vo.setContractTimeStr(item.getContractTime());
        vo.setRemarks(item.getRemarks());
        vo.setCreatedBy(item.getCreatedBy());
        vo.setCreatedTime(item.getCreatedTime());
        vo.setUpdatedBy(item.getUpdatedBy());
        vo.setUpdatedTime(item.getUpdatedTime());
        vo.setInactive(item.getInactive());
        vo.setInactiveStr(IEnum.getTextByCode(InactiveEnum.class, item.getInactive()));
        vo.setApprove(item.getApprove());
        vo.setApproveRemarks(item.getApproveRemarks());
        vo.setStartDate(item.getStartDate());
        vo.setEndDate(item.getEndDate());
        vo.setStartDateStr(item.getStartDate());
        vo.setEndDateStr(item.getEndDate());
        vo.setResponsibleUserId(item.getResponsibleUserId());
        vo.setFirstIsTalent(item.getFirstIsTalent());
        Date endDate = vo.getEndDate();
        if (ObjUtil.isNotNull(endDate)) {
            if (new Date().before(endDate)) {
                long day = DateUtil.betweenDay(new Date(), endDate, false);
                long l = day / 31;
                if (l > 0) {
                    vo.setDaysUntilDue(l + "个月" + (day % 31) + "天");
                } else {
                    vo.setDaysUntilDue(day + "天");
                }
            } else {
                long day = DateUtil.betweenDay(new Date(), endDate, false);
                long l = day / 31;
                if (l > 0) {
                    vo.setDaysUntilDue("已过期：" +l + "个月" + (day % 31) + "天");
                } else {
                    vo.setDaysUntilDue("已过期：" + day + "天");
                }
            }
        }
        return vo;
    }

    private void setUpdateFieldValue(BlazeOrderUpdateDTO item, BlazeOrderEntity entity){
        entity.setId(item.getId());
        entity.setTalentCertId(item.getTalentCertId());
        entity.setCustomerCertId(item.getCustomerCertId());
        entity.setContractTime(item.getContractTime());
        entity.setRemarks(item.getRemarks());
        entity.setInactive(item.getInactive());
        entity.setStartDate(item.getStartDate());
        entity.setEndDate(item.getEndDate());
        entity.setResponsibleUserId(item.getResponsibleUserId());
    }

    private BlazeOrderEntity convertToEntity(BlazeOrderAddDTO item){
        BlazeOrderEntity entity = new BlazeOrderEntity();
        entity.setTalentCertId(item.getTalentCertId());
        entity.setCustomerCertId(item.getCustomerCertId());
        entity.setContractTime(item.getContractTime());
        entity.setRemarks(item.getRemarks());
        entity.setInactive(item.getInactive());
        entity.setStartDate(item.getStartDate());
        entity.setEndDate(item.getEndDate());
        entity.setApprove(item.getApprove());
        entity.setResponsibleUserId(item.getResponsibleUserId());
        entity.setFirstIsTalent(item.getFirstIsTalent());
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(BlazeOrderAddDTO dto, List<MultipartFile> fileList) {
        BlazeOrderEntity entity = this.convertToEntity(dto);
        entity.setApprove(ApproveEnum.NOT_APPROVE.getCode());
        SerialNumberGenerateDTO generateDTO = new SerialNumberGenerateDTO();
        generateDTO.setCount(1);
        generateDTO.setSerialNumberId(2);
        List<String> generate = serialNumberService.generate(generateDTO);
        entity.setOrderNo(generate.get(0));
        baseRepository.save(entity);
        fileService.batchFileUpload(fileList, entity.getId(), BlazeOrderEntity.class, null);
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(BlazeOrderUpdateDTO dto, List<MultipartFile> fileList) {
        Integer id = dto.getId();
        BlazeOrderEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        if (!ObjUtil.equals(entity.getCustomerCertId(), dto.getCustomerCertId())
                || !ObjUtil.equals(entity.getTalentCertId(), dto.getTalentCertId())) {
            BlazeOrderDetailEntity detailEntity = blazeOrderDetailService.getByOrderId(id);
            if (ObjUtil.isNotNull(detailEntity)) {
                super.throwDataExistAssociated(id);
            }
        }
        this.setUpdateFieldValue(dto, entity);
        baseRepository.updateById(entity);
        fileService.batchFileUpload(fileList, entity.getId(), BlazeOrderEntity.class, dto.getDeleteFileIdList());
    }
    @Override
    public BlazeOrderVO detail(Integer id) {
        BlazeOrderEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        return this.convertToVO(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        BlazeOrderEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        BlazeOrderDetailEntity detailEntity = blazeOrderDetailService.getByOrderId(id);
        if (ObjUtil.isNotNull(detailEntity)) {
            super.throwDataExistAssociated(id);
        }
        baseRepository.removeById(id);
    }
}
