package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.OaEnterpriseEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.EnterpriseTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.EnterpriseVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.OaEnterpriseMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IOaEnterpriseService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Enterprise ServiceImpl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class OaEnterpriseServiceImpl
        extends BasicCurdServiceImpl<OaEnterpriseMapper, OaEnterpriseEntity> implements IOaEnterpriseService {

    @Resource
    private IUserService userService;
    @Resource
    private ICommonManager commonManager;

    @Override
    public PagedVO<EnterpriseVO> queryByPage(EnterpriseQueryDTO dto) {
        List<EnterpriseVO> voList = new ArrayList<>();
        Page<OaEnterpriseEntity> entityPage = this.selectPage(dto);
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
        List<OaEnterpriseEntity> list = super.list();
        if (CollUtil.isNotEmpty(list)) {
            LogicCheckUtil.validNameExist(null, dto.getEnterpriseName(),
                    list,entity -> dto.getEnterpriseName().equals(entity.getEnterpriseName()));
        }
        OaEnterpriseEntity entity = this.convertToEntity(dto);
        super.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(EnterpriseUpdateDTO dto) {
        Integer enterpriseId = dto.getEnterpriseId();
        OaEnterpriseEntity entity = super.mustGet(enterpriseId);
        LogicCheckUtil.validNameExist(enterpriseId, dto.getEnterpriseName(), super.list(),
                i -> !i.getId().equals(enterpriseId)
                        && dto.getEnterpriseName().equals(i.getEnterpriseName()));
        this.setUpdateFieldValue(dto, entity);
        super.updateById(entity);
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
        OaEnterpriseEntity entity = super.mustGet(enterpriseId);
        super.removeById(entity);
    }

    @Override
    public EnterpriseVO getDetail(Integer enterpriseId) {
        OaEnterpriseEntity entity = super.mustGet(enterpriseId);
        EnterpriseVO enterpriseVO = this.convertToVO(entity);
        Integer createdBy = entity.getCreatedBy();
        Map<Integer, String> nameMap = userService.getNameMap(ListUtil.of(createdBy));
        if (ObjUtil.isNotNull(createdBy)) {
            enterpriseVO.setCreateUserName(nameMap.get(createdBy));
        }
        return enterpriseVO;
    }

    @Override
    public boolean exportData(EnterpriseQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryByPage, StrUtil.EMPTY, this.getTitleList());
        return true;
    }


    private List<Pair<String, Func1<EnterpriseVO, ?>>> getTitleList() {
        return ListUtil.of(
                Pair.of("企业名称", EnterpriseVO::getEnterpriseName),
                Pair.of("企业logo", EnterpriseVO::getEnterpriseLogo),
                Pair.of("统一社会信用代码", EnterpriseVO::getUnifiedSocialCreditCode),
                Pair.of("企业类型", EnterpriseVO::getTypeName),
                Pair.of("联系人", EnterpriseVO::getContact),
                Pair.of("联系人电话", EnterpriseVO::getContactPhone),
                Pair.of("邮箱", EnterpriseVO::getEmail),
                Pair.of("状态", EnterpriseVO::getDisabledFlag),
                Pair.of("创建人", EnterpriseVO::getCreateUserName));
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

    public Page<OaEnterpriseEntity> selectPage(EnterpriseQueryDTO dto) {
        LambdaQueryWrapper<OaEnterpriseEntity> lambda = Wrappers.lambdaQuery();
        String keywords = dto.getKeywords();
        if (CharSequenceUtil.isNotBlank(keywords)) {
            lambda.and(i->i.like(OaEnterpriseEntity::getEnterpriseName, keywords)
                .or().like(OaEnterpriseEntity::getContact, keywords)
                .or().like(OaEnterpriseEntity::getContactPhone, keywords));
        }
        LocalDate startTime = dto.getStartTime();
        LocalDate endTime = dto.getEndTime();
        if (ObjectUtil.isAllNotEmpty(startTime, endTime)) {
            lambda.between(RelEntity::getCreatedTime, startTime,
                LocalDateTimeUtil.endOfDay(endTime.atStartOfDay()));
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }
}
