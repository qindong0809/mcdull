package io.gitee.dqcer.mcdull.blaze.web.service.impl;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.blaze.web.manager.IBlazeOrderRepository;
import io.gitee.dqcer.mcdull.blaze.web.manager.ICertificateRequirementsRepository;
import io.gitee.dqcer.mcdull.blaze.web.manager.ITalentCertificateRepository;
import io.gitee.dqcer.mcdull.blaze.model.bo.CertificateBO;
import io.gitee.dqcer.mcdull.blaze.model.entity.BlazeOrderEntity;
import io.gitee.dqcer.mcdull.blaze.model.entity.CertificateRequirementsEntity;
import io.gitee.dqcer.mcdull.blaze.model.entity.TalentCertificateEntity;
import io.gitee.dqcer.mcdull.blaze.model.enums.*;
import io.gitee.dqcer.mcdull.blaze.model.dto.*;
import io.gitee.dqcer.mcdull.blaze.model.vo.BlazeOrderDetailVO;
import io.gitee.dqcer.mcdull.blaze.model.vo.BlazeOrderVO;
import io.gitee.dqcer.mcdull.blaze.model.vo.CertificateRequirementsVO;
import io.gitee.dqcer.mcdull.blaze.web.service.*;
import io.gitee.dqcer.mcdull.blaze.util.CertificateUtil;
import io.gitee.dqcer.mcdull.common.CustomMultipartFile;
import io.gitee.dqcer.mcdull.common.pdf.ByteArrayInOutConvert;
import io.gitee.dqcer.mcdull.common.pdf.HtmlConvertPdf;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.mysql.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.framework.json.util.TimeZoneUtil;
import io.gitee.dqcer.mcdull.common.area.AreaFillUtil;
import io.gitee.dqcer.mcdull.common.excel.ExcelExportUtil;
import io.gitee.dqcer.mcdull.common.excel.ExcelTemplateUtil;
import io.gitee.dqcer.mcdull.blaze.util.TableNameUtil;
import io.gitee.dqcer.mcdull.system.facade.model.bo.DynamicFieldBO;
import io.gitee.dqcer.mcdull.system.facade.model.dto.ApproveDeptQueryDTO;
import io.gitee.dqcer.mcdull.system.facade.model.dto.FileBizDTO;
import io.gitee.dqcer.mcdull.system.facade.model.enums.FormItemControlTypeEnum;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ApproveDeptVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ApproveVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ExportContextVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.FileBinaryVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.FileSimpleVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.IFileVO;
import io.gitee.dqcer.mcdull.system.facade.service.AreaApi;
import io.gitee.dqcer.mcdull.system.facade.service.ApproveApi;
import io.gitee.dqcer.mcdull.system.facade.service.ExportApi;
import io.gitee.dqcer.mcdull.system.facade.service.FileRemoteApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import io.gitee.dqcer.mcdull.blaze.util.DropdownUtil;

/**
 * 证书需求表 Service
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
@Service
public class CertificateRequirementsServiceImpl
        extends BasicServiceImpl<ICertificateRequirementsRepository> implements ICertificateRequirementsService {

    @Resource
    private ICustomerInfoService customerInfoService;
    @Resource
    private AreaApi areaApi;
    @Resource
    private ApproveApi approveApi;
    @Resource
    private ExportApi exportApi;
    @Resource
    private FileRemoteApi fileRemoteApi;
    @Resource
    private IBlazeOrderRepository orderRepository;
    @Resource
    private IApproveService approveService;
    @Resource
    private ITalentCertificateRepository talentCertificateRepository;

    @Override
    public PagedVO<CertificateRequirementsVO> queryPage(CertificateRequirementsQueryDTO dto) {
        List<CertificateRequirementsVO> voList = new ArrayList<>();
        Page<CertificateRequirementsEntity> entityPage = baseRepository.selectPage(dto);
        List<CertificateRequirementsEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            List<LabelValueVO<Integer, String>> customerInfoList = customerInfoService.list();
            Map<Integer, CertificateBO> certificateMap = CertificateUtil.getCertificateMap();
            for (CertificateRequirementsEntity entity : recordList) {
                CertificateRequirementsVO vo = this.convertToVO(entity);
                BigDecimal positionContractPrice = entity.getPositionContractPrice();
                if (ObjUtil.isNotNull(positionContractPrice)) {
                    vo.setPositionContractPrice(positionContractPrice.setScale(2, RoundingMode.HALF_UP).toString());
                }
                BigDecimal otherCosts = entity.getOtherCosts();
                if (ObjUtil.isNotNull(otherCosts)) {
                    vo.setOtherCosts(otherCosts.setScale(2, RoundingMode.HALF_UP).toString());
                }
                BigDecimal actualPositionPrice = entity.getActualPositionPrice();
                if (ObjUtil.isNotNull(actualPositionPrice)) {
                    vo.setActualPositionPrice(actualPositionPrice.setScale(2, RoundingMode.HALF_UP).toString());
                }
                vo.setCustomerName(customerInfoList.stream()
                        .filter(v -> v.getValue().equals(vo.getCustomerId()))
                        .map(LabelValueVO::getLabel)
                        .findFirst().orElse(""));
                Integer certificateLevel = vo.getCertificateLevel();
                if (ObjUtil.isNotNull(certificateLevel)) {
                    CertificateBO certificateBO = certificateMap.get(certificateLevel);
                    if (ObjUtil.isNotNull(certificateBO)) {
                        vo.setCertificateLevelName(certificateBO.getName());
                        Integer specialty = vo.getSpecialty();
                        if (ObjUtil.isNotNull(specialty)) {
                            List<CertificateBO.Major> majorList = certificateBO.getMajorList();
                            String name = majorList.stream()
                                    .filter(v -> v.getCode().equals(specialty))
                                    .map(CertificateBO.Major::getName)
                                    .findFirst().orElse("");
                            vo.setSpecialtyName(name);
                        }
                    }
                }
                Integer title = vo.getTitle();
                if (ObjUtil.isNotNull(title)) {
                    vo.setTitleName(IEnum.getTextByCode(CertificateTitleEnum.class, title));
                }
                Integer initialOrTransfer = vo.getInitialOrTransfer();
                if (ObjUtil.isNotNull(initialOrTransfer)) {
                    vo.setInitialOrTransferName(IEnum.getTextByCode(CertificateInitialOrTransferEnum.class, initialOrTransfer));
                }
                Integer certificateStatus = vo.getCertificateStatus();
                if (ObjUtil.isNotNull(certificateStatus)) {
                    vo.setCertificateStatusName(IEnum.getTextByCode(CertificateStatusEnum.class, certificateStatus));
                }
                Integer biddingExit = vo.getBiddingExit();
                if (ObjUtil.isNotNull(biddingExit)) {
                    vo.setBiddingExitName(IEnum.getTextByCode(CertificateBiddingExitEnum.class, biddingExit));
                }
                Integer threePersonnel = vo.getThreePersonnel();
                if (ObjUtil.isNotNull(threePersonnel)) {
                    vo.setThreePersonnelName(IEnum.getTextByCode(CertificateThreePersonnerEnum.class, threePersonnel));
                }
                Integer socialSecurityRequirement = vo.getSocialSecurityRequirement();
                if (ObjUtil.isNotNull(socialSecurityRequirement)) {
                    vo.setSocialSecurityRequirementName(IEnum.getTextByCode(CertificateSocialSecurityRequirementEnum.class, socialSecurityRequirement));
                }
                Integer positionSource = vo.getPositionSource();
                if (ObjUtil.isNotNull(positionSource)) {
                    vo.setPositionSourceName(IEnum.getTextByCode(CertificatePositionSourceEnum.class, positionSource));
                }
                voList.add(vo);
            }
            AreaFillUtil.fill(voList, areaApi.nameMap(AreaFillUtil.collectCodes(voList)));
            approveService.setApproveVO(voList, recordList);
            fillFileVO(voList);
            fillDepartment(voList);
        }
        return PageUtil.toPage(voList, entityPage);
    }

    private void fillFileVO(List<CertificateRequirementsVO> voList) {
        if (CollUtil.isEmpty(voList)) {
            return;
        }
        FileBizDTO dto = new FileBizDTO();
        dto.setBizType(TableNameUtil.tableName(CertificateRequirementsEntity.class));
        dto.setBizIdList(voList.stream().map(IFileVO::getId).collect(Collectors.toList()));
        Map<Integer, List<FileSimpleVO>> fileMap = fileRemoteApi.listFiles(dto);
        for (CertificateRequirementsVO vo : voList) {
            vo.setFileList(fileMap.get(vo.getId()));
        }
    }

    private void fillDepartment(List<CertificateRequirementsVO> voList) {
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
        for (CertificateRequirementsVO vo : voList) {
            ApproveDeptVO dept = deptMap.get(vo.getResponsibleUserId());
            if (dept != null) {
                vo.setIsSameDepartment(dept.getIsSameDepartment());
                vo.setResponsibleUserDepartmentId(dept.getResponsibleUserDepartmentId());
                vo.setResponsibleUserDepartmentName(dept.getResponsibleUserDepartmentName());
                vo.setResponsibleUserPhone(dept.getResponsibleUserPhone());
            }
        }
    }

    @Override
    public boolean exportData(CertificateRequirementsQueryDTO dto) {
        ExportContextVO context = exportApi.context();
        List<CertificateRequirementsVO> list = this.queryPage(dto).getList();
        byte[] bytes = this.toExportBytes(list, context);
        try (var outputStream = ServletUtil.getResponse().getOutputStream()) {
            outputStream.write(bytes);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (Boolean.TRUE.equals(context.getAutoStorage())) {
            fileRemoteApi.uploadToSystemFolder(new CustomMultipartFile(
                    context.getMenuName() + ".xlsx", bytes));
        }
        return true;
    }

    private byte[] toExportBytes(List<CertificateRequirementsVO> list, ExportContextVO context) {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        ExcelExportUtil.write(outputStream, list, StrUtil.EMPTY,
                context.getOperatorName(), context.getDateTimeStr(), this.getTitleList());
        return outputStream.toByteArray();
    }

    @SuppressWarnings({"all"})
    @Override
    public void downloadTemplate() {
        Map<String, List<DynamicFieldBO>> sheetHeaderMap = new HashMap<>(8);
        List<DynamicFieldBO> fieldList = new ArrayList<>();
        fieldList.add(this.getCustomerFieldBO());
        fieldList.add(this.getCertificateFieldBO());
        fieldList.add(new DynamicFieldBO("specialtyName", "专业 *", true, FormItemControlTypeEnum.INPUT));
        fieldList.add(this.getProvinceFieldBO());
        fieldList.add(new DynamicFieldBO("cityName", "城市", true, FormItemControlTypeEnum.INPUT));
        fieldList.add(new DynamicFieldBO("quantity", "数量", true, FormItemControlTypeEnum.NUMBER));
        fieldList.add(new DynamicFieldBO("title", "职称", true, FormItemControlTypeEnum.SELECT, DropdownUtil.options(CertificateTitleEnum.class)));
        fieldList.add(new DynamicFieldBO("initialOrTransfer", "是否转正", true, FormItemControlTypeEnum.SELECT, DropdownUtil.options(CertificateInitialOrTransferEnum.class)));
        fieldList.add(new DynamicFieldBO("certificateStatus", "证书状态", true, FormItemControlTypeEnum.SELECT, DropdownUtil.options(CertificateStatusEnum.class)));
        fieldList.add(new DynamicFieldBO("positionContractPrice", "合同价", true, FormItemControlTypeEnum.NUMBER));
        fieldList.add(new DynamicFieldBO("otherCosts", "其他费用", true, FormItemControlTypeEnum.NUMBER));
        fieldList.add(new DynamicFieldBO("actualPositionPrice", "实际岗位价格", true, FormItemControlTypeEnum.NUMBER));
        fieldList.add(new DynamicFieldBO("duration", "期限", true, FormItemControlTypeEnum.NUMBER));
        fieldList.add(new DynamicFieldBO("biddingExit", "招标出场", true, FormItemControlTypeEnum.SELECT, DropdownUtil.options(CertificateBiddingExitEnum.class)));
        fieldList.add(new DynamicFieldBO("threePersonnel", "三类人员", true, FormItemControlTypeEnum.SELECT, DropdownUtil.options(CertificateThreePersonnerEnum.class)));
        fieldList.add(new DynamicFieldBO("socialSecurityRequirement", "社保要求", true, FormItemControlTypeEnum.SELECT, DropdownUtil.options(CertificateSocialSecurityRequirementEnum.class)));
        fieldList.add(new DynamicFieldBO("positionSource", "岗位来源", true, FormItemControlTypeEnum.SELECT, DropdownUtil.options(CertificatePositionSourceEnum.class)));
        fieldList.add(new DynamicFieldBO("remarks", "备注", true, FormItemControlTypeEnum.TEXTAREA));
        sheetHeaderMap.put("模板", fieldList);
        ExportContextVO context = exportApi.context();
        try (var outputStream = ServletUtil.getResponse().getOutputStream()) {
            ExcelTemplateUtil.write(outputStream, sheetHeaderMap,
                    context.getOperatorName(), context.getDateTimeStr());
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public List<LabelValueVO<Integer, String>> all(boolean isOrder) {
        CertificateRequirementsQueryDTO dto = new CertificateRequirementsQueryDTO();
        PageUtil.setMaxPageSize(dto);
        PagedVO<CertificateRequirementsVO> page = this.queryPage(dto);
        if (ObjUtil.isNotNull(page)) {
            List<BlazeOrderEntity> orderEntityList = CollUtil.defaultIfEmpty(orderRepository.list(), new ArrayList<>());
            Map<Integer, Boolean> map = orderRepository.getMapByCustomerCertId(orderEntityList.stream().map(BlazeOrderEntity::getCustomerCertId).collect(Collectors.toSet()));
            List<CertificateRequirementsVO> list = page.getList();
            if (CollUtil.isNotEmpty(list)) {
                List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
                for (CertificateRequirementsVO vo : list) {
                    if (BooleanUtil.isFalse(isOrder)) {
                        voList.add(new LabelValueVO<>(vo.getId(), vo.getPositionTitle()));
                        continue;
                    }
                    Boolean exist = map.get(vo.getId());
                    if (BooleanUtil.isFalse(exist)) {
                        voList.add(new LabelValueVO<>(vo.getId(), vo.getPositionTitle()));
                        continue;
                    }
                    Integer count = Convert.toInt(orderEntityList.stream().filter(item -> item.getCustomerCertId().equals(vo.getId())).count(), 0);
                    Integer quantity = vo.getQuantity();
                    if (quantity > count) {
                        voList.add(new LabelValueVO<>(vo.getId(), vo.getPositionTitle()));
                    }
                }
                return voList;
            }
        }
        return List.of();
    }

    @Override
    public Map<Integer, CertificateRequirementsEntity> map(Set<Integer> set) {
        if (CollUtil.isNotEmpty(set)) {
            List<CertificateRequirementsEntity> list = baseRepository.listByIds(set);
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().collect(Collectors.toMap(CertificateRequirementsEntity::getId, Function.identity()));
            }
        }
        return Map.of();
    }

    @Override
    public boolean existByCustomerId(Integer customerId) {
        return baseRepository.existByCustomerId(customerId);
    }

    @Override
    public void approve(ApproveDTO dto) {
        BlazeOrderEntity entity = orderRepository.getByCustomerCertId(dto.getId());
        approveService.approve(dto, baseRepository, ObjUtil.isNotNull(entity));
    }

    @Override
    public List<LabelValueVO<Integer, String>> okList() {
        CertificateRequirementsQueryDTO dto = new CertificateRequirementsQueryDTO();
        dto.setApprove(ApproveEnum.APPROVE.getCode());
        List<CertificateRequirementsVO> list = PageUtil.getAllList(dto, this::queryPage);
        List<BlazeOrderEntity> orderEntityList = CollUtil.defaultIfEmpty(orderRepository.list(), new ArrayList<>());
        Map<Integer, Boolean> map = orderRepository.getMapByCustomerCertId(orderEntityList.stream().map(BlazeOrderEntity::getCustomerCertId).collect(Collectors.toSet()));
        if (CollUtil.isNotEmpty(list)) {
            List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
            for (CertificateRequirementsVO vo : list) {
                Boolean exist = map.get(vo.getId());
                if (BooleanUtil.isFalse(exist)) {
                    voList.add(new LabelValueVO<>(vo.getId(), vo.getPositionTitle()));
                    continue;
                }
                Integer currentCount = Convert.toInt(orderEntityList.stream().filter(item -> item.getCustomerCertId().equals(vo.getId())).count(), 0);
                Integer configCount = vo.getQuantity();
                if (configCount > currentCount) {
                    voList.add(new LabelValueVO<>(vo.getId(), vo.getPositionTitle()));
                }
            }
            return voList;
        }
        return List.of();
    }

    @Override
    public List<LabelValueVO<Integer, String>> getList(Integer talentCertId) {
        CertificateRequirementsQueryDTO dto = new CertificateRequirementsQueryDTO();
        dto.setApprove(ApproveEnum.APPROVE.getCode());
        if (ObjUtil.isNotNull(talentCertId)) {
            TalentCertificateEntity talentCertificateEntity = talentCertificateRepository.getById(talentCertId);
            if (ObjUtil.isNotNull(talentCertificateEntity)) {
                dto.setCertificateLevel(talentCertificateEntity.getCertificateLevel());
            }
        }
        List<CertificateRequirementsVO> list = PageUtil.getAllList(dto, this::queryPage);
        if (CollUtil.isNotEmpty(list)) {
            List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
            for (CertificateRequirementsVO vo : list) {
                voList.add(new LabelValueVO<>(vo.getId(), vo.getPositionTitle()));
            }
            return voList;
        }
        return List.of();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer copy(Integer id) {
        CertificateRequirementsEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        CertificateRequirementsAddDTO addDTO = this.convertAddDTO(entity);
        addDTO.setResponsibleUserId(entity.getResponsibleUserId());
        addDTO.setPositionTitle(StrUtil.format("{}({})", entity.getPositionTitle(), 1));
        List<MultipartFile> multipartFileList = this.convertFileList(id);
        this.insert(addDTO, multipartFileList);
        return id;
    }

    private List<MultipartFile> convertFileList(Integer id) {
        List<MultipartFile> fileList = new ArrayList<>();
        FileBizDTO dto = new FileBizDTO();
        dto.setBizId(id);
        dto.setBizType(TableNameUtil.tableName(CertificateRequirementsEntity.class));
        List<FileBinaryVO> fileDateList = fileRemoteApi.downloadFiles(dto);
        if (CollUtil.isNotEmpty(fileDateList)) {
            for (FileBinaryVO item : fileDateList) {
                MultipartFile multipartFile = new CustomMultipartFile(item.getFileName(), item.getContent());
                fileList.add(multipartFile);
            }
        }
        return fileList;
    }

    private CertificateRequirementsAddDTO convertAddDTO(CertificateRequirementsEntity entity) {
        CertificateRequirementsAddDTO addDTO = new CertificateRequirementsAddDTO();
        addDTO.setCustomerId(entity.getCustomerId());
        addDTO.setCertificateLevel(entity.getCertificateLevel());
        addDTO.setSpecialty(entity.getSpecialty());
        addDTO.setProvincesCode(entity.getProvincesCode());
        addDTO.setCityCode(entity.getCityCode());
        addDTO.setQuantity(entity.getQuantity());
        addDTO.setTitle(entity.getTitle());
        addDTO.setInitialOrTransfer(entity.getInitialOrTransfer());
        addDTO.setCertificateStatus(entity.getCertificateStatus());
        addDTO.setPositionContractPrice(entity.getPositionContractPrice());
        addDTO.setOtherCosts(entity.getOtherCosts());
        addDTO.setActualPositionPrice(entity.getActualPositionPrice());
        addDTO.setDuration(entity.getDuration());
        addDTO.setBiddingExit(entity.getBiddingExit());
        addDTO.setThreePersonnel(entity.getThreePersonnel());
        addDTO.setSocialSecurityRequirement(entity.getSocialSecurityRequirement());
        addDTO.setPositionSource(entity.getPositionSource());
        addDTO.setRemarks(entity.getRemarks());
        return addDTO;
    }

    @Override
    public CertificateRequirementsEntity get(Integer id) {
        return baseRepository.getById(id);
    }

    private DynamicFieldBO getProvinceFieldBO() {
        DynamicFieldBO fieldBO = new DynamicFieldBO("provincesName", "省份", true, FormItemControlTypeEnum.SELECT);
        List<LabelValueVO<String, String>> list = areaApi.provinceList();
        fieldBO.setDropdownList(list.stream().map(LabelValueVO::getLabel).collect(Collectors.toList()));
        fieldBO.setExtraObj(list);
        return fieldBO;
    }

    private DynamicFieldBO getCertificateFieldBO() {
        DynamicFieldBO fieldBO = new DynamicFieldBO("certificateLevel", "证书等级", true, FormItemControlTypeEnum.SELECT);
        Map<Integer, CertificateBO> certificateMap = CertificateUtil.getCertificateMap();
        fieldBO.setDropdownList(certificateMap.values().stream().map(CertificateBO::getName).collect(Collectors.toList()));
        fieldBO.setExtraObj(certificateMap);
        return fieldBO;
    }

    private DynamicFieldBO getCustomerFieldBO() {
        DynamicFieldBO fieldBO = new DynamicFieldBO("customerName", "客户名称", true, FormItemControlTypeEnum.SELECT);
        List<LabelValueVO<Integer, String>> customerList = customerInfoService.list();
        fieldBO.setDropdownList(customerList.stream().map(LabelValueVO::getLabel).collect(Collectors.toList()));
        fieldBO.setExtraObj(customerList);
        return fieldBO;
    }

    private List<Pair<String, Func1<CertificateRequirementsVO, ?>>> getTitleList() {
        return ListUtil.toList(
                Pair.of("客户名称", CertificateRequirementsVO::getCustomerName),
                Pair.of("证书等级", CertificateRequirementsVO::getCertificateLevelName),
                Pair.of("专业", CertificateRequirementsVO::getSpecialtyName),
                Pair.of("省份", CertificateRequirementsVO::getProvincesName),
                Pair.of("城市", CertificateRequirementsVO::getCityName),
                Pair.of("数量", CertificateRequirementsVO::getQuantity),
                Pair.of("职称", CertificateRequirementsVO::getTitleName),
                Pair.of("初始/转正", CertificateRequirementsVO::getInitialOrTransferName),
                Pair.of("证书状态", CertificateRequirementsVO::getCertificateStatusName),
                Pair.of("合同价", CertificateRequirementsVO::getPositionContractPrice),
                Pair.of("其他费用", CertificateRequirementsVO::getOtherCosts),
                Pair.of("实际岗位价格", CertificateRequirementsVO::getActualPositionPrice),
                Pair.of("期限", CertificateRequirementsVO::getDuration),
                Pair.of("招标出场", CertificateRequirementsVO::getBiddingExitName),
                Pair.of("三类人员", CertificateRequirementsVO::getThreePersonnelName),
                Pair.of("社保要求", CertificateRequirementsVO::getSocialSecurityRequirementName),
                Pair.of("岗位来源", CertificateRequirementsVO::getPositionSourceName),
                Pair.of("备注", CertificateRequirementsVO::getRemarks),
                Pair.of("创建时间", CertificateRequirementsVO::getCreatedTime)
        );
    }

    private CertificateRequirementsVO convertToVO(CertificateRequirementsEntity item){
        CertificateRequirementsVO vo = new CertificateRequirementsVO();
        vo.setId(item.getId());
        vo.setCustomerId(item.getCustomerId());
        vo.setCertificateLevel(item.getCertificateLevel());
        vo.setSpecialty(item.getSpecialty());
        vo.setProvincesCode(item.getProvincesCode());
        vo.setCityCode(item.getCityCode());
        vo.setQuantity(item.getQuantity());
        vo.setTitle(item.getTitle());
        vo.setInitialOrTransfer(item.getInitialOrTransfer());
        vo.setCertificateStatus(item.getCertificateStatus());
        vo.setDuration(item.getDuration());
        vo.setBiddingExit(item.getBiddingExit());
        vo.setThreePersonnel(item.getThreePersonnel());
        vo.setSocialSecurityRequirement(item.getSocialSecurityRequirement());
        vo.setPositionSource(item.getPositionSource());
        vo.setPositionTitle(item.getPositionTitle());
        vo.setRemarks(item.getRemarks());
        vo.setCreatedTime(item.getCreatedTime());
        return vo;
    }

    private void setUpdateFieldValue(CertificateRequirementsUpdateDTO item, CertificateRequirementsEntity entity){
        entity.setId(item.getId());
        entity.setCustomerId(item.getCustomerId());
        entity.setCertificateLevel(item.getCertificateLevel());
        entity.setSpecialty(item.getSpecialty());
        entity.setProvincesCode(item.getProvincesCode());
        entity.setCityCode(item.getCityCode());
        entity.setQuantity(item.getQuantity());
        entity.setTitle(item.getTitle());
        entity.setInitialOrTransfer(item.getInitialOrTransfer());
        entity.setCertificateStatus(item.getCertificateStatus());
        entity.setPositionContractPrice(item.getPositionContractPrice());
        entity.setOtherCosts(item.getOtherCosts());
        entity.setActualPositionPrice(item.getActualPositionPrice());
        entity.setDuration(item.getDuration());
        entity.setBiddingExit(item.getBiddingExit());
        entity.setThreePersonnel(item.getThreePersonnel());
        entity.setSocialSecurityRequirement(item.getSocialSecurityRequirement());
        entity.setPositionSource(item.getPositionSource());
        entity.setPositionTitle(item.getPositionTitle());
        entity.setRemarks(item.getRemarks());
        entity.setResponsibleUserId(item.getResponsibleUserId());
    }

    private CertificateRequirementsEntity convertToEntity(CertificateRequirementsAddDTO item){
        CertificateRequirementsEntity entity = new CertificateRequirementsEntity();
        entity.setCustomerId(item.getCustomerId());
        entity.setCertificateLevel(item.getCertificateLevel());
        entity.setSpecialty(item.getSpecialty());
        entity.setProvincesCode(item.getProvincesCode());
        entity.setCityCode(item.getCityCode());
        entity.setQuantity(item.getQuantity());
        entity.setTitle(item.getTitle());
        entity.setInitialOrTransfer(item.getInitialOrTransfer());
        entity.setCertificateStatus(item.getCertificateStatus());
        entity.setPositionContractPrice(item.getPositionContractPrice());
        entity.setOtherCosts(item.getOtherCosts());
        entity.setActualPositionPrice(item.getActualPositionPrice());
        entity.setDuration(item.getDuration());
        entity.setBiddingExit(item.getBiddingExit());
        entity.setThreePersonnel(item.getThreePersonnel());
        entity.setSocialSecurityRequirement(item.getSocialSecurityRequirement());
        entity.setPositionSource(item.getPositionSource());
        entity.setPositionTitle(item.getPositionTitle());
        entity.setRemarks(item.getRemarks());
        entity.setResponsibleUserId(item.getResponsibleUserId());
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(CertificateRequirementsAddDTO dto, List<MultipartFile> fileList) {
        CertificateRequirementsEntity entity = this.convertToEntity(dto);
        entity.setApprove(ApproveEnum.NOT_APPROVE.getCode());
        entity.setPositionTitle(StrUtil.EMPTY);
        baseRepository.save(entity);
        String positionTitle = dto.getPositionTitle();
        if (StrUtil.isBlank(positionTitle)) {
            this.builderPositionTitle(entity);
        } else {
            entity.setPositionTitle(positionTitle);
            baseRepository.updateById(entity);
        }
        this.saveFiles(entity.getId(), fileList, null);
    }

    private void saveFiles(Integer bizId, List<MultipartFile> fileList, List<Integer> deleteFileIdList) {
        FileBizDTO dto = new FileBizDTO();
        dto.setBizId(bizId);
        dto.setBizType(TableNameUtil.tableName(CertificateRequirementsEntity.class));
        dto.setDeleteFileIdList(deleteFileIdList);
        fileRemoteApi.batchUpload(dto, fileList);
    }

    private void builderPositionTitle(CertificateRequirementsEntity entity) {
        CertificateRequirementsQueryDTO dto = new CertificateRequirementsQueryDTO();
        PageUtil.setMaxPageSize(dto);
        PagedVO<CertificateRequirementsVO> page = this.queryPage(dto);
        if (ObjUtil.isNotNull(page)) {
            List<CertificateRequirementsVO> list = page.getList();
            CertificateRequirementsVO vo = list.stream().filter(i -> i.getId().equals(entity.getId())).findFirst().orElse(null);
            if (ObjUtil.isNotNull(vo)) {
                String positionTitle = StrUtil.format("{}|{}|{}|{}|{}|{}|{}|{}", vo.getCustomerName(), vo.getCertificateLevelName(),
                        vo.getSpecialtyName(), vo.getSocialSecurityRequirementName(), vo.getActualPositionPrice(),
                        vo.getDuration(), vo.getInitialOrTransferName(), vo.getThreePersonnelName());
                entity.setPositionTitle(positionTitle);
                baseRepository.updateById(entity);
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(CertificateRequirementsUpdateDTO dto, List<MultipartFile> fileList) {
        Integer id = dto.getId();
        CertificateRequirementsEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.setUpdateFieldValue(dto, entity);
        baseRepository.updateById(entity);
        String positionTitle = dto.getPositionTitle();
        if (StrUtil.isBlank(positionTitle)) {
            this.builderPositionTitle(entity);
        }
        this.saveFiles(entity.getId(), fileList, dto.getDeleteFileIdList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Integer> idList) {
        List<CertificateRequirementsEntity> entityList = baseRepository.queryListByIds(idList);
        if (entityList.size() != idList.size()) {
            this.throwDataNotExistException(idList);
        }
        boolean exist = orderRepository.existByCertificateRequirementsIdList(idList);
        if (exist) {
            super.throwDataExistAssociated(idList);
        }
        baseRepository.removeBatchByIds(idList);
        for (CertificateRequirementsEntity entity : entityList) {
            FileBizDTO dto = new FileBizDTO();
            dto.setBizId(entity.getId());
            dto.setBizType(TableNameUtil.tableName(CertificateRequirementsEntity.class));
            fileRemoteApi.remove(dto);
        }
    }
}
