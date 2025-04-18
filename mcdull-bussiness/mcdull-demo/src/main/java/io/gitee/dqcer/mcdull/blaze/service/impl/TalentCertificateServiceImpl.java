package io.gitee.dqcer.mcdull.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.blaze.dao.repository.ITalentCertificateRepository;
import io.gitee.dqcer.mcdull.blaze.domain.bo.CertificateBO;
import io.gitee.dqcer.mcdull.blaze.domain.entity.BlazeOrderEntity;
import io.gitee.dqcer.mcdull.blaze.domain.entity.CertificateRequirementsEntity;
import io.gitee.dqcer.mcdull.blaze.domain.entity.TalentCertificateEntity;
import io.gitee.dqcer.mcdull.blaze.domain.enums.*;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentCertificateAddDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentCertificateQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentCertificateUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.TalentCertificateVO;
import io.gitee.dqcer.mcdull.blaze.service.*;
import io.gitee.dqcer.mcdull.business.common.CustomMultipartFile;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.bo.DynamicFieldBO;
import io.gitee.dqcer.mcdull.system.provider.model.enums.FormItemControlTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAreaManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IAreaService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFileService;
import io.gitee.dqcer.mcdull.blaze.util.CertificateUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 证书需求表 Service
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
@Service
public class TalentCertificateServiceImpl
        extends BasicServiceImpl<ITalentCertificateRepository> implements ITalentCertificateService {

    @Resource
    private IAreaManager areaManager;
    @Resource
    private ITalentService talentService;
    @Resource
    private ICommonManager commonManager;
    @Resource
    private IAreaService areaService;
    @Resource
    private IFileService fileService;
    @Resource
    private ICertificateRequirementsService certificateRequirementsService;
    @Resource
    private IBlazeOrderService orderService;
    @Resource
    private IApproveService approveService;

    public PagedVO<TalentCertificateVO> queryPage(TalentCertificateQueryDTO dto) {
        List<TalentCertificateVO> voList = new ArrayList<>();
        Page<TalentCertificateEntity> entityPage = baseRepository.selectPage(dto);
        List<TalentCertificateEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            List<LabelValueVO<Integer, String>> talentList = talentService.list();
            Map<Integer, CertificateBO> certificateMap = CertificateUtil.getCertificateMap();
            for (TalentCertificateEntity entity : recordList) {
                TalentCertificateVO vo = this.convertToVO(entity);
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
                vo.setTalentName(talentList.stream()
                        .filter(v -> v.getValue().equals(vo.getTalentId()))
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
                areaManager.set(voList);

            }
            approveService.setApproveVO(voList, recordList);
            commonManager.setFileVO(voList, TalentCertificateEntity.class);
            commonManager.setDepartment(voList, UserContextHolder.userId());
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public void exportData(TalentCertificateQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitleList());
    }

    private List<Pair<String, Func1<TalentCertificateVO, ?>>> getTitleList() {
        return Arrays.asList(
                Pair.of("客户名称", TalentCertificateVO::getTalentName),
                Pair.of("证书等级", TalentCertificateVO::getCertificateLevelName),
                Pair.of("专业", TalentCertificateVO::getSpecialtyName),
                Pair.of("省份", TalentCertificateVO::getProvincesName),
                Pair.of("城市", TalentCertificateVO::getCityName),
                Pair.of("职称", TalentCertificateVO::getTitleName),
                Pair.of("初始/转正", TalentCertificateVO::getInitialOrTransferName),
                Pair.of("证书状态", TalentCertificateVO::getCertificateStatusName),
                Pair.of("合同价", TalentCertificateVO::getPositionContractPrice),
                Pair.of("其他费用", TalentCertificateVO::getOtherCosts),
                Pair.of("实际岗位价格", TalentCertificateVO::getActualPositionPrice),
                Pair.of("期限", TalentCertificateVO::getDuration),
                Pair.of("招标出场", TalentCertificateVO::getBiddingExitName),
                Pair.of("三类人员", TalentCertificateVO::getThreePersonnelName),
                Pair.of("社保要求", TalentCertificateVO::getSocialSecurityRequirementName),
                Pair.of("岗位来源", TalentCertificateVO::getPositionSourceName),
                Pair.of("备注", TalentCertificateVO::getRemarks),
                Pair.of("创建时间", TalentCertificateVO::getCreatedTime)
        );
    }

    @Override
    public void downloadTemplate() {
        Map<String, List<DynamicFieldBO>> sheetHeaderMap = new HashMap<>(8);
        List<DynamicFieldBO> fieldList = new ArrayList<>();
        fieldList.add(this.getCustomerFieldBO());
        fieldList.add(this.getCertificateFieldBO());
        fieldList.add(new DynamicFieldBO("specialtyName", "专业 *", true, FormItemControlTypeEnum.INPUT));
        fieldList.add(this.getProvinceFieldBO());
        fieldList.add(new DynamicFieldBO("cityName", "城市", true, FormItemControlTypeEnum.INPUT));
        fieldList.add(new DynamicFieldBO("title", "职称", true, FormItemControlTypeEnum.SELECT, CertificateTitleEnum.class));
        fieldList.add(new DynamicFieldBO("initialOrTransfer", "是否转正", true, FormItemControlTypeEnum.SELECT, CertificateInitialOrTransferEnum.class));
        fieldList.add(new DynamicFieldBO("certificateStatus", "证书状态", true, FormItemControlTypeEnum.SELECT, CertificateStatusEnum.class));
        fieldList.add(new DynamicFieldBO("positionContractPrice", "合同价", true, FormItemControlTypeEnum.NUMBER));
        fieldList.add(new DynamicFieldBO("otherCosts", "其他费用", true, FormItemControlTypeEnum.NUMBER));
        fieldList.add(new DynamicFieldBO("actualPositionPrice", "实际岗位价格", true, FormItemControlTypeEnum.NUMBER));
        fieldList.add(new DynamicFieldBO("duration", "期限", true, FormItemControlTypeEnum.NUMBER));
        fieldList.add(new DynamicFieldBO("biddingExit", "招标出场", true, FormItemControlTypeEnum.SELECT, CertificateBiddingExitEnum.class));
        fieldList.add(new DynamicFieldBO("threePersonnel", "三类人员", true, FormItemControlTypeEnum.SELECT, CertificateThreePersonnerEnum.class));
        fieldList.add(new DynamicFieldBO("socialSecurityRequirement", "社保要求", true, FormItemControlTypeEnum.SELECT, CertificateSocialSecurityRequirementEnum.class));
        fieldList.add(new DynamicFieldBO("positionSource", "岗位来源", true, FormItemControlTypeEnum.SELECT, CertificatePositionSourceEnum.class));
        fieldList.add(new DynamicFieldBO("remarks", "备注", true, FormItemControlTypeEnum.TEXTAREA));
        sheetHeaderMap.put("模板", fieldList);
        commonManager.downloadExcelTemplate(sheetHeaderMap, "证书需求模板");
    }

    @Override
    public List<LabelValueVO<Integer, String>> list(Integer customerCertId, boolean isFilter) {
        TalentCertificateQueryDTO dto = new TalentCertificateQueryDTO();
        if (ObjUtil.isNotNull(customerCertId)) {
            CertificateRequirementsEntity entity = certificateRequirementsService.get(customerCertId);
            dto.setCertificateLevel(entity.getCertificateLevel());
            dto.setSpecialty(entity.getSpecialty());
            dto.setSocialSecurityRequirement(entity.getSocialSecurityRequirement());
            dto.setApprove(ApproveEnum.APPROVE.getCode());
        }
        PageUtil.setMaxPageSize(dto);
        PagedVO<TalentCertificateVO> page = this.queryPage(dto);
        if (ObjUtil.isNotNull(page)) {
            List<TalentCertificateVO> list = page.getList();
            if (CollUtil.isNotEmpty(list)) {
                Map<Integer, Boolean> map = orderService.getMapByTalentCertId(list.stream().map(TalentCertificateVO::getId).collect(Collectors.toSet()));
                List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
                for (TalentCertificateVO vo : list) {
                    Integer id = vo.getId();
                    Boolean exist = map.get(id);
                    if (BooleanUtil.isTrue(isFilter)) {
                        if (BooleanUtil.isFalse(exist) || ObjUtil.isNull(exist)) {
                            voList.add(new LabelValueVO<>(id, vo.getPositionTitle()));
                        }
                    } else {
                        voList.add(new LabelValueVO<>(id, vo.getPositionTitle()));
                    }
                }
                return voList;
            }
        }
        return List.of();
    }

    @Override
    public Map<Integer, TalentCertificateEntity> map(Set<Integer> set) {
        if (CollUtil.isNotEmpty(set)) {
            List<TalentCertificateEntity> list = baseRepository.listByIds(set);
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().collect(Collectors.toMap(TalentCertificateEntity::getId, Function.identity()));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public boolean existByTalentId(Integer talentId) {
        if (ObjUtil.isNotNull(talentId)) {
            LambdaQueryWrapper<TalentCertificateEntity> query = Wrappers.lambdaQuery();
            query.eq(TalentCertificateEntity::getTalentId, talentId);
            return baseRepository.exists(query);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approve(ApproveDTO dto) {
        BlazeOrderEntity entity = orderService.getByTalentCertId(dto.getId());
        approveService.approve(dto, baseRepository, ObjUtil.isNotNull(entity));
    }

    @Override
    public List<LabelValueVO<Integer, String>> getList(Integer customerCertId) {
        TalentCertificateQueryDTO dto = new TalentCertificateQueryDTO();
        dto.setApprove(ApproveEnum.APPROVE.getCode());
        if (ObjUtil.isNotNull(customerCertId)) {
            CertificateRequirementsEntity entity = certificateRequirementsService.get(customerCertId);
            dto.setCertificateLevel(entity.getCertificateLevel());
        }
        List<TalentCertificateVO> list = PageUtil.getAllList(dto, this::queryPage);
        if (CollUtil.isNotEmpty(list)) {
            List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
            for (TalentCertificateVO vo : list) {
                Integer id = vo.getId();
                voList.add(new LabelValueVO<>(id, vo.getPositionTitle()));
            }
            return voList;
        }
        return List.of();
    }

    @Override
    public TalentCertificateEntity get(Integer talentCertId) {
        return baseRepository.getById(talentCertId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer copy(Integer id) {
        TalentCertificateEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        TalentCertificateAddDTO addDTO = this.convertAddDTO(entity);
        addDTO.setResponsibleUserId(entity.getResponsibleUserId());
        addDTO.setPositionTitle(StrUtil.format("{}({})", entity.getPositionTitle(), 1));
        List<MultipartFile> multipartFileList = this.convertFileList(id);
        this.insert(addDTO, multipartFileList);
       return id;
    }

    private List<MultipartFile> convertFileList(Integer id) {
        List<MultipartFile> fileList = new ArrayList<>();
        List<Pair<String, byte[]>> fileDateList = commonManager.getFileDateList(id, TalentCertificateEntity.class);
        if (CollUtil.isNotEmpty(fileDateList)) {
            for (Pair<String, byte[]> pair : fileDateList) {
                MultipartFile multipartFile = new CustomMultipartFile(pair.getKey(), pair.getValue());
                fileList.add(multipartFile);
            }
        }
        return fileList;
    }

    private TalentCertificateAddDTO convertAddDTO(TalentCertificateEntity entity) {
        TalentCertificateAddDTO addDTO = new TalentCertificateAddDTO();
        addDTO.setTalentId(entity.getTalentId());
        addDTO.setCertificateLevel(entity.getCertificateLevel());
        addDTO.setSpecialty(entity.getSpecialty());
        addDTO.setProvincesCode(entity.getProvincesCode());
        addDTO.setCityCode(entity.getCityCode());
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
        addDTO.setPositionTitle(entity.getPositionTitle());
        addDTO.setRemarks(entity.getRemarks());
        return addDTO;
    }

    private DynamicFieldBO getProvinceFieldBO() {
        DynamicFieldBO fieldBO = new DynamicFieldBO("provincesName", "省份", true, FormItemControlTypeEnum.SELECT);
        List<LabelValueVO<String, String>> list = areaService.provinceList();
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
        List<LabelValueVO<Integer, String>> customerList = talentService.list();
        fieldBO.setDropdownList(customerList.stream().map(LabelValueVO::getLabel).collect(Collectors.toList()));
        fieldBO.setExtraObj(customerList);
        return fieldBO;
    }

    private TalentCertificateVO convertToVO(TalentCertificateEntity item){
        TalentCertificateVO vo = new TalentCertificateVO();
        vo.setId(item.getId());
        vo.setTalentId(item.getTalentId());
        vo.setCertificateLevel(item.getCertificateLevel());
        vo.setSpecialty(item.getSpecialty());
        vo.setProvincesCode(item.getProvincesCode());
        vo.setCityCode(item.getCityCode());
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

    private void setUpdateFieldValue(TalentCertificateUpdateDTO item, TalentCertificateEntity entity){
        entity.setId(item.getId());
        entity.setTalentId(item.getTalentId());
        entity.setCertificateLevel(item.getCertificateLevel());
        entity.setSpecialty(item.getSpecialty());
        entity.setProvincesCode(item.getProvincesCode());
        entity.setCityCode(item.getCityCode());
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
        entity.setRemarks(item.getRemarks());
        entity.setResponsibleUserId(item.getResponsibleUserId());
    }

    private TalentCertificateEntity convertToEntity(TalentCertificateAddDTO item){
        TalentCertificateEntity entity = new TalentCertificateEntity();
        entity.setTalentId(item.getTalentId());
        entity.setCertificateLevel(item.getCertificateLevel());
        entity.setSpecialty(item.getSpecialty());
        entity.setProvincesCode(item.getProvincesCode());
        entity.setCityCode(item.getCityCode());
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
        entity.setRemarks(item.getRemarks());
        entity.setResponsibleUserId(item.getResponsibleUserId());
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(TalentCertificateAddDTO dto, List<MultipartFile> fileList) {
        TalentCertificateEntity entity = this.convertToEntity(dto);
        entity.setPositionTitle(StrUtil.EMPTY);
        entity.setApprove(ApproveEnum.NOT_APPROVE.getCode());
        baseRepository.save(entity);
        String positionTitle = StrUtil.isBlank(dto.getPositionTitle()) ? this.builderPositionTitle(entity) : dto.getPositionTitle();
        entity.setPositionTitle(positionTitle);
        baseRepository.updateById(entity);
        fileService.batchFileUpload(fileList, entity.getId(), TalentCertificateEntity.class, null);
    }

    private String builderPositionTitle(TalentCertificateEntity entity) {
        TalentCertificateQueryDTO dto = new TalentCertificateQueryDTO();
        PageUtil.setMaxPageSize(dto);
        PagedVO<TalentCertificateVO> page = this.queryPage(dto);
        if (ObjUtil.isNotNull(page)) {
            List<TalentCertificateVO> list = page.getList();
            TalentCertificateVO vo = list.stream().filter(i -> i.getId().equals(entity.getId())).findFirst().orElse(null);
            if (ObjUtil.isNotNull(vo)) {
                return StrUtil.format("{}|{}|{}|{}|{}|{}|{}|{}", vo.getTalentName(), vo.getCertificateLevelName(),
                        vo.getSpecialtyName(), vo.getSocialSecurityRequirementName(), vo.getActualPositionPrice(),
                        vo.getDuration(), vo.getInitialOrTransferName(), vo.getThreePersonnelName());
            }
        }
        return StrUtil.EMPTY;
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(TalentCertificateUpdateDTO dto, List<MultipartFile> fileList) {
        Integer id = dto.getId();
        TalentCertificateEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.setUpdateFieldValue(dto, entity);
        baseRepository.updateById(entity);
        String positionTitle = StrUtil.isBlank(dto.getPositionTitle()) ? this.builderPositionTitle(entity) : dto.getPositionTitle();
        entity.setPositionTitle(positionTitle);
        baseRepository.updateById(entity);
        Integer delFileId = dto.getDelFileId();
        fileService.batchFileUpload(fileList, entity.getId(), TalentCertificateEntity.class, ListUtil.of(delFileId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Integer> idList) {
        List<TalentCertificateEntity> entityList = baseRepository.queryListByIds(idList);
        if (entityList.size() != idList.size()) {
            this.throwDataNotExistException(idList);
        }
        boolean exist = orderService.existByTalentCertificateId(idList);
        if (exist) {
            super.throwDataExistAssociated(idList);
        }
        baseRepository.removeBatchByIds(idList);
        for (TalentCertificateEntity entity : entityList) {
            fileService.remove(entity.getId(), TalentCertificateEntity.class);
        }
    }
}
