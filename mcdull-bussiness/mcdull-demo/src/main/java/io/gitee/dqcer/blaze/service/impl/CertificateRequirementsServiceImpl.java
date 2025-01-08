package io.gitee.dqcer.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.blaze.dao.repository.ICertificateRequirementsRepository;
import io.gitee.dqcer.blaze.domain.entity.CertificateRequirementsEntity;
import io.gitee.dqcer.blaze.domain.enums.*;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsAddDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsQueryDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.CertificateRequirementsVO;
import io.gitee.dqcer.blaze.service.ICertificateRequirementsService;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAreaManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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

    public PagedVO<CertificateRequirementsVO> queryPage(CertificateRequirementsQueryDTO dto) {
        List<CertificateRequirementsVO> voList = new ArrayList<>();
        Page<CertificateRequirementsEntity> entityPage = baseRepository.selectPage(dto);
        List<CertificateRequirementsEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            Set<String> provinceSet = recordList.stream().map(CertificateRequirementsEntity::getProvince).filter(ObjUtil::isNotNull).collect(Collectors.toSet());
            Set<String> all = new HashSet<>(provinceSet);
            Set<String> citySet = recordList.stream().map(CertificateRequirementsEntity::getCity).filter(ObjUtil::isNotNull).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(citySet)) {
                all.addAll(citySet);
            }
            Map<String, String> areaMap = areaManager.map(all);
            for (CertificateRequirementsEntity entity : recordList) {
                CertificateRequirementsVO vo = this.convertToVO(entity);
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
        }
        return PageUtil.toPage(voList, entityPage);
    }

    private CertificateRequirementsVO convertToVO(CertificateRequirementsEntity item){
        CertificateRequirementsVO vo = new CertificateRequirementsVO();
        vo.setId(item.getId());
        vo.setCertificateLevel(item.getCertificateLevel());
        vo.setSpecialty(item.getSpecialty());
        vo.setProvince(item.getProvince());
        vo.setCity(item.getCity());
        vo.setQuantity(item.getQuantity());
        vo.setTitle(item.getTitle());
        vo.setInitialOrTransfer(item.getInitialOrTransfer());
        vo.setCertificateStatus(item.getCertificateStatus());
        vo.setPositionContractPrice(item.getPositionContractPrice());
        vo.setOtherCosts(item.getOtherCosts());
        vo.setActualPositionPrice(item.getActualPositionPrice());
        vo.setDuration(item.getDuration());
        vo.setBiddingExit(item.getBiddingExit());
        vo.setThreePersonnel(item.getThreePersonnel());
        vo.setSocialSecurityRequirement(item.getSocialSecurityRequirement());
        vo.setPositionSource(item.getPositionSource());
        vo.setPositionTitle(item.getPositionTitle());
        vo.setRemarks(item.getRemarks());
        return vo;
    }

    private void setUpdateFieldValue(CertificateRequirementsUpdateDTO item, CertificateRequirementsEntity entity){
        entity.setId(item.getId());
        entity.setCertificateLevel(item.getCertificateLevel());
        entity.setSpecialty(item.getSpecialty());
        entity.setProvince(item.getProvince());
        entity.setCity(item.getCity());
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
    }

    private CertificateRequirementsEntity convertToEntity(CertificateRequirementsAddDTO item){
            CertificateRequirementsEntity entity = new CertificateRequirementsEntity();
        entity.setCertificateLevel(item.getCertificateLevel());
        entity.setSpecialty(item.getSpecialty());
        entity.setProvince(item.getProvince());
        entity.setCity(item.getCity());
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
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(CertificateRequirementsAddDTO dto) {
        CertificateRequirementsEntity entity = this.convertToEntity(dto);
        baseRepository.save(entity);
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
    }
    @Override
    public CertificateRequirementsVO detail(Integer id) {
        CertificateRequirementsEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        return this.convertToVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Integer> idList) {
        List<CertificateRequirementsEntity> entityList = baseRepository.queryListByIds(idList);
        if (entityList.size() != idList.size()) {
            this.throwDataNotExistException(idList);
        }
        baseRepository.removeBatchByIds(idList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        CertificateRequirementsEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        baseRepository.removeById(id);
    }
}
