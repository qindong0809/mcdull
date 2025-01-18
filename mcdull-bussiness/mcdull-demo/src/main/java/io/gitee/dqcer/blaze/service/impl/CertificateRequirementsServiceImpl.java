package io.gitee.dqcer.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.blaze.dao.repository.ICertificateRequirementsRepository;
import io.gitee.dqcer.blaze.domain.bo.CertificateBO;
import io.gitee.dqcer.blaze.domain.entity.CertificateRequirementsEntity;
import io.gitee.dqcer.blaze.domain.enums.*;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsAddDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsQueryDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.CertificateRequirementsVO;
import io.gitee.dqcer.blaze.service.ICertificateRequirementsService;
import io.gitee.dqcer.blaze.service.ICustomerInfoService;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.bo.DynamicFieldBO;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FormItemControlTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAreaManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IAreaService;
import io.gitee.dqcer.util.CertificateUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CertificateRequirementsServiceImpl
        extends BasicServiceImpl<ICertificateRequirementsRepository> implements ICertificateRequirementsService {

    @Resource
    private IAreaManager areaManager;

    @Resource
    private ICustomerInfoService customerInfoService;

    @Resource
    private ICommonManager commonManager;

    @Resource
    private IAreaService areaService;

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
                areaManager.set(voList);

            }
        }
        return PageUtil.toPage(voList, entityPage);
    }
    public void demo() {
        CertificateRequirementsEntity entity = new CertificateRequirementsEntity();

    }

    @Override
    public void exportData() {
        CertificateRequirementsQueryDTO dto = new CertificateRequirementsQueryDTO();
        PageUtil.setMaxPageSize(dto);
        Map<String, String> titleMap = this.getTitleMap();
        List<CertificateRequirementsVO> list = CollUtil.emptyIfNull(this.queryPage(dto).getList());
        List<Map<String, String>> mapList = new ArrayList<>();
        for (CertificateRequirementsVO vo : list) {
            Map<String, String> map = new HashMap<>();
            map.put("customerName", vo.getCustomerName());
            map.put("certificateLevelName", vo.getCertificateLevelName());
            map.put("specialtyName", vo.getSpecialtyName());
            map.put("provincesName", vo.getProvincesName());
            map.put("cityName", vo.getCityName());
            map.put("quantity", StrUtil.toString(vo.getQuantity()));
            map.put("titleName", vo.getTitleName());
            map.put("initialOrTransferName", vo.getInitialOrTransferName());
            map.put("certificateStatusName", vo.getCertificateStatusName());
            map.put("positionContractPrice", StrUtil.toString(vo.getPositionContractPrice()));
            map.put("otherCosts", StrUtil.toString(vo.getOtherCosts()));
            map.put("actualPositionPrice", StrUtil.toString(vo.getActualPositionPrice()));
            map.put("duration", StrUtil.toString(vo.getDuration()));
            map.put("biddingExitName", vo.getBiddingExitName());
            map.put("threePersonnelName", vo.getThreePersonnelName());
            map.put("socialSecurityRequirementName", vo.getSocialSecurityRequirementName());
            map.put("positionSourceName", vo.getPositionSourceName());
            map.put("remarks", vo.getRemarks());
            map.put("createdTime", commonManager.convertDateByUserTimezone(vo.getCreatedTime()));
            mapList.add(map);
        }
        commonManager.exportExcel("企业证书需求", "", titleMap, mapList);
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
        fieldList.add(new DynamicFieldBO("quantity", "数量", true, FormItemControlTypeEnum.NUMBER));
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
    public List<LabelValueVO<Integer, String>> list() {
        CertificateRequirementsQueryDTO dto = new CertificateRequirementsQueryDTO();
        PageUtil.setMaxPageSize(dto);
        PagedVO<CertificateRequirementsVO> page = this.queryPage(dto);
        if (ObjUtil.isNotNull(page)) {
            List<CertificateRequirementsVO> list = page.getList();
            if (CollUtil.isNotEmpty(list)) {
                List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
                for (CertificateRequirementsVO vo : list) {
                    voList.add(new LabelValueVO<>(vo.getId(), vo.getPositionTitle()));
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
    public CertificateRequirementsEntity get(Integer id) {
        return baseRepository.getById(id);
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
        List<LabelValueVO<Integer, String>> customerList = customerInfoService.list();
        fieldBO.setDropdownList(customerList.stream().map(LabelValueVO::getLabel).collect(Collectors.toList()));
        fieldBO.setExtraObj(customerList);
        return fieldBO;
    }

    private Map<String, String> getTitleMap() {
        Map<String, String> titleMap = new LinkedHashMap<>();
        titleMap.put("客户名称", "customerName");
        titleMap.put("证书等级", "certificateLevelName");
        titleMap.put("专业", "specialtyName");
        titleMap.put("省份", "provincesName");
        titleMap.put("城市", "cityName");
        titleMap.put("数量", "quantity");
        titleMap.put("职称", "titleName");
        titleMap.put("初始/转正", "initialOrTransferName");
        titleMap.put("证书状态", "certificateStatusName");
        titleMap.put("合同价", "positionContractPrice");
        titleMap.put("其他费用", "otherCosts");
        titleMap.put("实际岗位价格", "actualPositionPrice");
        titleMap.put("期限", "duration");
        titleMap.put("招标出场", "biddingExitName");
        titleMap.put("三类人员", "threePersonnelName");
        titleMap.put("社保要求", "socialSecurityRequirementName");
        titleMap.put("岗位来源", "positionSourceName");
        titleMap.put("备注", "remark");
        titleMap.put("创建时间", "createdTime");
        titleMap.put("更新时间", "updatedTime");
        return titleMap;
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
        entity.setRemarks(item.getRemarks());
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
        entity.setRemarks(item.getRemarks());
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(CertificateRequirementsAddDTO dto) {
        CertificateRequirementsEntity entity = this.convertToEntity(dto);
        entity.setPositionTitle(StrUtil.EMPTY);
        baseRepository.save(entity);
        this.builderPositionTitle(entity);
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
    public void update(CertificateRequirementsUpdateDTO dto) {
        Integer id = dto.getId();
        CertificateRequirementsEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.setUpdateFieldValue(dto, entity);
        baseRepository.updateById(entity);
        this.builderPositionTitle(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Integer> idList) {
        List<CertificateRequirementsEntity> entityList = baseRepository.queryListByIds(idList);
        if (entityList.size() != idList.size()) {
            this.throwDataNotExistException(idList);
        }
        baseRepository.removeBatchByIds(idList);
    }
}
