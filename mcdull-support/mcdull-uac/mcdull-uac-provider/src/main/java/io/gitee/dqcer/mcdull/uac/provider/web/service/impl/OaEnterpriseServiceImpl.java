package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EnterpriseAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EnterpriseQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EnterpriseUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OaEnterpriseEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.EnterpriseTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.EnterpriseVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IOaEnterpriseRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IOaEnterpriseService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Enterprise ServiceImpl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class OaEnterpriseServiceImpl
        extends BasicServiceImpl<IOaEnterpriseRepository> implements IOaEnterpriseService {

    @Resource
    private IUserService userService;
    @Resource
    private ICommonManager commonManager;

    @Override
    public PagedVO<EnterpriseVO> queryByPage(EnterpriseQueryDTO dto) {
        List<EnterpriseVO> voList = new ArrayList<>();
        Page<OaEnterpriseEntity> entityPage = baseRepository.selectPage(dto);
        List<OaEnterpriseEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            Set<Integer> userIdSet = recordList.stream().map(BaseEntity::getCreatedBy).collect(Collectors.toSet());
            Map<Integer, String> nameMap = userService.getNameMap(new ArrayList<>(userIdSet));
            for (OaEnterpriseEntity entity : recordList) {
                EnterpriseVO vo = this.convertToVO(entity);
                Integer createdBy = entity.getCreatedBy();
                if (ObjUtil.isNotNull(createdBy)) {
                    vo.setCreateUserName(nameMap.get(createdBy));
                }
                vo.setTypeName(IEnum.getTextByCode(EnterpriseTypeEnum.class, entity.getType()));
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(EnterpriseAddDTO dto) {
        List<OaEnterpriseEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(null, dto.getEnterpriseName(),
                    list,entity -> dto.getEnterpriseName().equals(entity.getEnterpriseName()));
        }
        OaEnterpriseEntity entity = this.convertToEntity(dto);
        baseRepository.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(EnterpriseUpdateDTO dto) {
        Integer enterpriseId = dto.getEnterpriseId();
        OaEnterpriseEntity entity = baseRepository.getById(enterpriseId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(enterpriseId);
        }
        this.validNameExist(enterpriseId, dto.getEnterpriseName(), baseRepository.list(),
                i -> !i.getId().equals(enterpriseId)
                        && dto.getEnterpriseName().equals(i.getEnterpriseName()));
        this.setUpdateFieldValue(dto, entity);
        baseRepository.updateById(entity);
    }

    private void setUpdateFieldValue(EnterpriseUpdateDTO dto, OaEnterpriseEntity entity) {
        entity.setEnterpriseName(dto.getEnterpriseName());
        entity.setEnterpriseLogo(dto.getEnterpriseLogo());
        entity.setType(dto.getType());
        entity.setUnifiedSocialCreditCode(dto.getUnifiedSocialCreditCode());
        entity.setContact(dto.getContact());
        entity.setContactPhone(dto.getContactPhone());
        entity.setEmail(dto.getEmail());
        entity.setProvince(dto.getProvince());
        entity.setProvinceName(dto.getProvinceName());
        entity.setCity(dto.getCity());
        entity.setCityName(dto.getCityName());
        entity.setDistrict(dto.getDistrict());
        entity.setDistrictName(dto.getDistrictName());
        entity.setAddress(dto.getAddress());
        entity.setBusinessLicense(dto.getBusinessLicense());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer enterpriseId) {
        OaEnterpriseEntity entity = baseRepository.getById(enterpriseId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(enterpriseId);
        }
        baseRepository.removeById(entity);
    }

    @Override
    public EnterpriseVO getDetail(Integer enterpriseId) {
        OaEnterpriseEntity entity = baseRepository.getById(enterpriseId);
        EnterpriseVO enterpriseVO = this.convertToVO(entity);
        Integer createdBy = entity.getCreatedBy();
        Map<Integer, String> nameMap = userService.getNameMap(ListUtil.of(createdBy));
        if (ObjUtil.isNotNull(createdBy)) {
            enterpriseVO.setCreateUserName(nameMap.get(createdBy));
        }
        return enterpriseVO;
    }

    @Override
    public void exportData(EnterpriseQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryByPage, StrUtil.EMPTY, this.getTitleMap());
    }


    private Map<String, Func1<EnterpriseVO, ?>> getTitleMap() {
        Map<String, Func1<EnterpriseVO, ?>> titleMap = new HashMap<>(8);
        titleMap.put("企业名称", EnterpriseVO::getEnterpriseName);
        titleMap.put("企业logo", EnterpriseVO::getEnterpriseLogo);
        titleMap.put("统一社会信用代码", EnterpriseVO::getUnifiedSocialCreditCode);
        titleMap.put("企业类型", EnterpriseVO::getTypeName);
        titleMap.put("联系人", EnterpriseVO::getContact);
        titleMap.put("联系人电话", EnterpriseVO::getContactPhone);
        titleMap.put("邮箱", EnterpriseVO::getEmail);
        titleMap.put("状态", EnterpriseVO::getDisabledFlag);
        titleMap.put("创建人", EnterpriseVO::getCreateUserName);
        return titleMap;
    }

    private OaEnterpriseEntity convertToEntity(EnterpriseAddDTO dto) {
        OaEnterpriseEntity oaEnterpriseEntity = new OaEnterpriseEntity();
        oaEnterpriseEntity.setEnterpriseName(dto.getEnterpriseName());
        oaEnterpriseEntity.setEnterpriseLogo(dto.getEnterpriseLogo());
        oaEnterpriseEntity.setType(dto.getType());
        oaEnterpriseEntity.setUnifiedSocialCreditCode(dto.getUnifiedSocialCreditCode());
        oaEnterpriseEntity.setContact(dto.getContact());
        oaEnterpriseEntity.setContactPhone(dto.getContactPhone());
        oaEnterpriseEntity.setEmail(dto.getEmail());
        oaEnterpriseEntity.setProvince(dto.getProvince());
        oaEnterpriseEntity.setProvinceName(dto.getProvinceName());
        oaEnterpriseEntity.setCity(dto.getCity());
        oaEnterpriseEntity.setCityName(dto.getCityName());
        oaEnterpriseEntity.setDistrict(dto.getDistrict());
        oaEnterpriseEntity.setDistrictName(dto.getDistrictName());
        oaEnterpriseEntity.setAddress(dto.getAddress());
        oaEnterpriseEntity.setBusinessLicense(dto.getBusinessLicense());
        return oaEnterpriseEntity;
    }

    private EnterpriseVO convertToVO(OaEnterpriseEntity entity) {
        EnterpriseVO enterpriseVO = new EnterpriseVO();
        enterpriseVO.setEnterpriseId(Convert.toInt(entity.getId()));
        enterpriseVO.setEnterpriseName(entity.getEnterpriseName());
        enterpriseVO.setEnterpriseLogo(entity.getEnterpriseLogo());
        enterpriseVO.setUnifiedSocialCreditCode(entity.getUnifiedSocialCreditCode());
        enterpriseVO.setType(entity.getType());
        enterpriseVO.setContact(entity.getContact());
        enterpriseVO.setContactPhone(entity.getContactPhone());
        enterpriseVO.setEmail(entity.getEmail());
        enterpriseVO.setProvince(entity.getProvince());
        enterpriseVO.setProvinceName(entity.getProvinceName());
        enterpriseVO.setCity(entity.getCity());
        enterpriseVO.setCityName(entity.getCityName());
        enterpriseVO.setDistrict(entity.getDistrict());
        enterpriseVO.setDistrictName(entity.getDistrictName());
        enterpriseVO.setAddress(entity.getAddress());
        enterpriseVO.setBusinessLicense(entity.getBusinessLicense());
        enterpriseVO.setDisabledFlag(entity.getInactive());
        enterpriseVO.setCreateUserId(entity.getCreatedBy());
        enterpriseVO.setCreateTime(LocalDateTimeUtil.of(entity.getCreatedTime()));
        enterpriseVO.setUpdateTime(LocalDateTimeUtil.of(entity.getUpdatedTime()));
        return enterpriseVO;
    }
}
