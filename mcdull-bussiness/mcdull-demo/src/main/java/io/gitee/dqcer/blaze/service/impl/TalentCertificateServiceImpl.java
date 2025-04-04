package io.gitee.dqcer.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.blaze.dao.repository.ITalentCertificateRepository;
import io.gitee.dqcer.blaze.domain.bo.CertificateBO;
import io.gitee.dqcer.blaze.domain.entity.CertificateRequirementsEntity;
import io.gitee.dqcer.blaze.domain.entity.TalentCertificateEntity;
import io.gitee.dqcer.blaze.domain.enums.*;
import io.gitee.dqcer.blaze.domain.form.TalentCertificateAddDTO;
import io.gitee.dqcer.blaze.domain.form.TalentCertificateQueryDTO;
import io.gitee.dqcer.blaze.domain.form.TalentCertificateUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.FileVO;
import io.gitee.dqcer.blaze.domain.vo.TalentCertificateVO;
import io.gitee.dqcer.blaze.service.IBlazeOrderService;
import io.gitee.dqcer.blaze.service.ICertificateRequirementsService;
import io.gitee.dqcer.blaze.service.ITalentCertificateService;
import io.gitee.dqcer.blaze.service.ITalentService;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.bo.DynamicFieldBO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FormItemControlTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFileBizRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAreaManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IAreaService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFolderService;
import io.gitee.dqcer.util.CertificateUtil;
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
    private IFileBizRepository fileBizRepository;
    @Resource
    private IBlazeOrderService orderService;
    @Resource
    private IFolderService folderService;

    private static final String DEFAULT_BIZ_CODD = "blaze-talent-cert";

    public PagedVO<TalentCertificateVO> queryPage(TalentCertificateQueryDTO dto) {
        List<TalentCertificateVO> voList = new ArrayList<>();
        Page<TalentCertificateEntity> entityPage = baseRepository.selectPage(dto);
        List<TalentCertificateEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            String fileDomainName = commonManager.getConfig("file-domain-name");
            List<LabelValueVO<Integer, String>> talentList = talentService.list();
            Map<Integer, CertificateBO> certificateMap = CertificateUtil.getCertificateMap();
            List<Integer> idList = recordList.stream().map(IdEntity::getId).collect(Collectors.toList());
            Map<Integer, List<FileEntity>> fileEntityMap = fileService.get(idList, TalentCertificateEntity.class);
            for (TalentCertificateEntity entity : recordList) {
                TalentCertificateVO vo = this.convertToVO(entity);
                List<FileEntity> fileEntityList = fileEntityMap.get(entity.getId());
                List<FileVO> fileList = new ArrayList<>();
                if (CollUtil.isNotEmpty(fileEntityList)) {
                    for (FileEntity fileEntity : fileEntityList) {
                        FileVO fileVO = new FileVO();
                        fileVO.setUrl(URLUtil.encodeBlank(fileDomainName + "/upload/" + fileEntity.getFileKey()));
                        fileVO.setFileName(fileEntity.getFileName());
                        fileVO.setId(fileEntity.getId());
                        fileList.add(fileVO);
                    }
                }
                vo.setFileList(fileList);
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
//            dto.setBiddingExit(entity.getBiddingExit());
//            dto.setThreePersonnel(entity.getThreePersonnel());
            dto.setSocialSecurityRequirement(entity.getSocialSecurityRequirement());
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
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(TalentCertificateAddDTO dto, MultipartFile file) {
        TalentCertificateEntity entity = this.convertToEntity(dto);
        entity.setPositionTitle(StrUtil.EMPTY);
        baseRepository.save(entity);
        this.builderPositionTitle(entity);
        if (ObjUtil.isNotNull(file)) {
            fileService.fileUpload(file, folderService.getSystemFolderId(), entity.getId(), TalentCertificateEntity.class);
        }
    }

    private void builderPositionTitle(TalentCertificateEntity entity) {
        TalentCertificateQueryDTO dto = new TalentCertificateQueryDTO();
        PageUtil.setMaxPageSize(dto);
        PagedVO<TalentCertificateVO> page = this.queryPage(dto);
        if (ObjUtil.isNotNull(page)) {
            List<TalentCertificateVO> list = page.getList();
            TalentCertificateVO vo = list.stream().filter(i -> i.getId().equals(entity.getId())).findFirst().orElse(null);
            if (ObjUtil.isNotNull(vo)) {
                String positionTitle = StrUtil.format("{}|{}|{}|{}|{}|{}|{}|{}", vo.getTalentName(), vo.getCertificateLevelName(),
                        vo.getSpecialtyName(), vo.getSocialSecurityRequirementName(), vo.getActualPositionPrice(),
                        vo.getDuration(), vo.getInitialOrTransferName(), vo.getThreePersonnelName());

                entity.setPositionTitle(positionTitle);
                baseRepository.updateById(entity);
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(TalentCertificateUpdateDTO dto, MultipartFile file) {
        Integer id = dto.getId();
        TalentCertificateEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.setUpdateFieldValue(dto, entity);
        baseRepository.updateById(entity);
        this.builderPositionTitle(entity);

        Integer delFileId = dto.getDelFileId();
        if (ObjUtil.isNotNull(delFileId)) {
            fileService.remove(delFileId, id, TalentCertificateEntity.class);
        }

        if (ObjUtil.isNotNull(file)) {
            fileService.fileUpload(file, folderService.getSystemFolderId(), entity.getId(), TalentCertificateEntity.class);
        }
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
        Map<Integer, List<Integer>> map = fileBizRepository.mapByBizCode(DEFAULT_BIZ_CODD);
        for (TalentCertificateEntity entity : entityList) {
            Integer entityId = entity.getId();
            List<Integer> fileIdList = map.get(entityId);
            if (CollUtil.isNotEmpty(fileIdList)) {
                for (Integer fileId : fileIdList) {
                    fileService.removeByFileId(fileId, entityId, DEFAULT_BIZ_CODD);
                }
            }
        }
    }
}
