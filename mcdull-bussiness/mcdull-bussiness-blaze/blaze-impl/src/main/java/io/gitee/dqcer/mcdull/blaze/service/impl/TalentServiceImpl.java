package io.gitee.dqcer.mcdull.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.blaze.dao.repository.ITalentRepository;
import io.gitee.dqcer.mcdull.blaze.domain.entity.TalentEntity;
import io.gitee.dqcer.mcdull.blaze.domain.enums.CertificateSocialSecurityRequirementEnum;
import io.gitee.dqcer.mcdull.blaze.domain.enums.CertificateTitleEnum;
import io.gitee.dqcer.mcdull.blaze.domain.enums.TalentWorkUnitTypeEnum;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentAddDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.TalentVO;
import io.gitee.dqcer.mcdull.blaze.service.ITalentCertificateService;
import io.gitee.dqcer.mcdull.blaze.service.ITalentService;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.enums.GenderEnum;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAreaManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IUserManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private ITalentCertificateService talentCertificateService;
    @Resource
    private ICommonManager commonManager;
    @Resource
    private IUserManager userManager;

    public PagedVO<TalentVO> queryPage(TalentQueryDTO dto) {
        List<TalentVO> voList = new ArrayList<>();
        Page<TalentEntity> entityPage = baseRepository.selectPage(dto);
        List<TalentEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            Set<Integer> collect = recordList.stream().map(TalentEntity::getResponsibleUserId).collect(Collectors.toSet());
            Map<Integer, String> map = userManager.getNameMap(new ArrayList<>(collect));
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
                Integer responsibleUserId = entity.getResponsibleUserId();
                if (ObjUtil.isNotNull(responsibleUserId)) {
                    vo.setResponsibleUserId(responsibleUserId);
                    vo.setResponsibleUserIdStr(map.get(responsibleUserId));
                }
                String s = DesensitizedUtil.idCardNum(vo.getIdNumber(), 3, 2);
                vo.setIdNumber(s);
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

    @Override
    public void exportData(TalentQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitleList());
    }

    private List<Pair<String, Func1<TalentVO, ?>>> getTitleList() {
        List<Pair<String, Func1<TalentVO, ?>>> list = new ArrayList<>();
        list.add(Pair.of("姓名", TalentVO::getName));
        list.add(Pair.of("身份证号", TalentVO::getIdNumber));
        list.add(Pair.of("工作单位性质", TalentVO::getWorkUnitTypeName));
        list.add(Pair.of("社保状态", TalentVO::getSocialSecurityStatusName));
        list.add(Pair.of("所在地省代码", TalentVO::getProvincesCode));
        list.add(Pair.of("所在地省名称", TalentVO::getProvincesName));
        list.add(Pair.of("所在市代码", TalentVO::getCityCode));
        list.add(Pair.of("所在市名称", TalentVO::getCityName));
        list.add(Pair.of("性别", TalentVO::getGenderName));
        list.add(Pair.of("职称", TalentVO::getTitleName));
        list.add(Pair.of("创建时间", TalentVO::getCreatedTime));
        list.add(Pair.of("更新时间", TalentVO::getUpdatedTime));
        return list;
    }

    private TalentVO convertToVO(TalentEntity item){
        TalentVO vo = new TalentVO();
        vo.setId(item.getId());
        vo.setName(item.getName());
        vo.setIdNumber(item.getIdNumber());
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
        entity.setResponsibleUserId(item.getResponsibleUserId());
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
        entity.setResponsibleUserId(item.getResponsibleUserId());
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
        TalentVO vo = this.convertToVO(entity);
        vo.setContactNumber(entity.getContactNumber());
        return vo;
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        TalentEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        boolean exist = talentCertificateService.existByTalentId(id);
        if (exist) {
            super.throwDataExistAssociated(id);
        }
        baseRepository.removeById(id);
    }
}
