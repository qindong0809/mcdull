package io.gitee.dqcer.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.blaze.dao.repository.ITalentRepository;
import io.gitee.dqcer.blaze.domain.entity.TalentEntity;
import io.gitee.dqcer.blaze.domain.enums.CertificateSocialSecurityRequirementEnum;
import io.gitee.dqcer.blaze.domain.enums.CertificateTitleEnum;
import io.gitee.dqcer.blaze.domain.enums.TalentWorkUnitTypeEnum;
import io.gitee.dqcer.blaze.domain.form.TalentAddDTO;
import io.gitee.dqcer.blaze.domain.form.TalentQueryDTO;
import io.gitee.dqcer.blaze.domain.form.TalentUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.TalentVO;
import io.gitee.dqcer.blaze.service.ITalentService;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.GenderEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAreaManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 人才表 Service
 *
 * @author dqcer
 * @since 2025-01-10 19:52:20
 */

@Service
public class TalentServiceImpl
        extends BasicServiceImpl<ITalentRepository> implements ITalentService {

    @Resource
    private IAreaManager areaManager;

    public PagedVO<TalentVO> queryPage(TalentQueryDTO dto) {
        List<TalentVO> voList = new ArrayList<>();
        Page<TalentEntity> entityPage = baseRepository.selectPage(dto);
        List<TalentEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            for (TalentEntity entity : recordList) {
                TalentVO vo = this.convertToVO(entity);
                Integer gender = vo.getGender();
                if (ObjUtil.isNotNull(gender)) {
                    vo.setGenderName(IEnum.getTextByCode(GenderEnum.class, gender));
                }
                Integer socialSecurityStatus = vo.getSocialSecurityStatus();
                if (ObjUtil.isNotNull(socialSecurityStatus)) {
                    vo.setSocialSecurityStatusName(IEnum.getTextByCode(CertificateSocialSecurityRequirementEnum.class, socialSecurityStatus));
                }
                Integer workUnitType = vo.getWorkUnitType();
                if (ObjUtil.isNotNull(workUnitType)) {
                    vo.setWorkUnitTypeName(IEnum.getTextByCode(TalentWorkUnitTypeEnum.class, workUnitType));
                }
                Integer title = vo.getTitle();
                if (ObjUtil.isNotNull(title)) {
                    vo.setTitleName(IEnum.getTextByCode(CertificateTitleEnum.class, title));
                }
                voList.add(vo);
            }
            areaManager.set(voList);
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public List<LabelValueVO<Integer, String>> list() {
        List<TalentEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            List<LabelValueVO<Integer, String>> labelValueVOList = new ArrayList<>();
            for (TalentEntity talentEntity : list) {
                LabelValueVO<Integer, String> labelValueVO = new LabelValueVO<>(talentEntity.getId(), talentEntity.getName());
                labelValueVOList.add(labelValueVO);
            }
            return labelValueVOList;
        }
        return Collections.emptyList();
    }

    private TalentVO convertToVO(TalentEntity item){
        TalentVO vo = new TalentVO();
        vo.setId(item.getId());
        vo.setName(item.getName());
        vo.setIdNumber(item.getIdNumber());
        vo.setContactNumber(item.getContactNumber());
        vo.setWorkUnitType(item.getWorkUnitType());
        vo.setSocialSecurityStatus(item.getSocialSecurityStatus());
        vo.setProvincesCode(item.getProvincesCode());
        vo.setCityCode(item.getCityCode());
        vo.setGender(item.getGender());
        vo.setTitle(item.getTitle());
        vo.setCreatedTime(item.getCreatedTime());
        vo.setUpdatedTime(item.getUpdatedTime());
        return vo;
    }

    private void setUpdateFieldValue(TalentUpdateDTO item, TalentEntity entity){
        entity.setId(item.getId());
        entity.setName(item.getName());
        entity.setIdNumber(item.getIdNumber());
        entity.setContactNumber(item.getContactNumber());
        entity.setWorkUnitType(item.getWorkUnitType());
        entity.setSocialSecurityStatus(item.getSocialSecurityStatus());
        entity.setProvincesCode(item.getProvincesCode());
        entity.setCityCode(item.getCityCode());
        entity.setGender(item.getGender());
        entity.setTitle(item.getTitle());
    }

    private TalentEntity convertToEntity(TalentAddDTO item){
            TalentEntity entity = new TalentEntity();
        entity.setName(item.getName());
        entity.setIdNumber(item.getIdNumber());
        entity.setContactNumber(item.getContactNumber());
        entity.setWorkUnitType(item.getWorkUnitType());
        entity.setSocialSecurityStatus(item.getSocialSecurityStatus());
        entity.setProvincesCode(item.getProvincesCode());
        entity.setCityCode(item.getCityCode());
        entity.setGender(item.getGender());
        entity.setTitle(item.getTitle());
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(TalentAddDTO dto) {
        TalentEntity entity = this.convertToEntity(dto);
        baseRepository.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(TalentUpdateDTO dto) {
        Integer id = dto.getId();
        TalentEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.setUpdateFieldValue(dto, entity);
        baseRepository.updateById(entity);
    }
    @Override
    public TalentVO detail(Integer id) {
        TalentEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        return this.convertToVO(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        TalentEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        baseRepository.removeById(id);
    }
}
