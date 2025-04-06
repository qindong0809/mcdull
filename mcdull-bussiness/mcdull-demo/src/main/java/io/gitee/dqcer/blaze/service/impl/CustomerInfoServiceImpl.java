package io.gitee.dqcer.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.blaze.dao.repository.ICustomerInfoRepository;
import io.gitee.dqcer.blaze.domain.entity.CustomerInfoEntity;
import io.gitee.dqcer.blaze.domain.form.CustomerInfoAddDTO;
import io.gitee.dqcer.blaze.domain.form.CustomerInfoQueryDTO;
import io.gitee.dqcer.blaze.domain.form.CustomerInfoUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.CustomerInfoVO;
import io.gitee.dqcer.blaze.service.ICertificateRequirementsService;
import io.gitee.dqcer.blaze.service.ICustomerInfoService;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.DictSelectTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAreaManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IDictTypeManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 企业信息 Service
 *
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */

@Service
public class CustomerInfoServiceImpl
        extends BasicServiceImpl<ICustomerInfoRepository> implements ICustomerInfoService {

    @Resource
    private IDictTypeManager dictTypeManager;
    @Resource
    private IUserManager userManager;
    @Resource
    private IAreaManager areaManager;
    @Resource
    private ICertificateRequirementsService certificateRequirementsService;
    @Resource
    private ICommonManager commonManager;

    public PagedVO<CustomerInfoVO> queryPage(CustomerInfoQueryDTO dto) {
        List<CustomerInfoVO> voList = new ArrayList<>();
        Page<CustomerInfoEntity> entityPage = baseRepository.selectPage(dto);
        List<CustomerInfoEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            Set<Integer> collect = recordList.stream().map(CustomerInfoEntity::getResponsibleUserId).collect(Collectors.toSet());
            Map<Integer, String> nameMap1 = userManager.getNameMap(new ArrayList<>(collect));
            Map<Integer, String> nameMap = userManager.getMap(recordList);
            for (CustomerInfoEntity entity : recordList) {
                CustomerInfoVO vo = this.convertToVO(entity);
                KeyValueBO<String, String> keyValue = dictTypeManager.dictVO(DictSelectTypeEnum.CUSTOMER_TYPE, vo.getCustomerType());
                if (ObjUtil.isNotNull(keyValue)) {
                    vo.setCustomerTypeName(keyValue.getValue());
                }
                KeyValueBO<String, String> inactiveKeyValue = dictTypeManager.dictVO(DictSelectTypeEnum.IN_ACTIVE, vo.getInactive().toString());
                if (ObjUtil.isNotNull(inactiveKeyValue)) {
                    vo.setInactiveName(inactiveKeyValue.getValue());
                }
                vo.setResponsibleUserId(entity.getResponsibleUserId());
                vo.setResponsibleUserIdStr(nameMap1.get(vo.getResponsibleUserId()));
                vo.setCreatedName(nameMap.get(vo.getCreatedBy()));
                vo.setUpdatedName(nameMap.get(vo.getUpdatedBy()));
                vo.setPhoneNumber("");
                voList.add(vo);
            }
            areaManager.set(voList);
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public List<LabelValueVO<Integer, String>> list() {
        List<CustomerInfoEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            List<LabelValueVO<Integer, String>> voList = new ArrayList<>();
            for (CustomerInfoEntity entity : list) {
                voList.add(new LabelValueVO<>(entity.getId(), entity.getName()));
            }
            voList.sort(Comparator.comparing(LabelValueVO::getLabel));
            return voList;
        }
        return Collections.emptyList();
    }

    @Override
    public void exportData(CustomerInfoQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitleList());
    }

    private List<Pair<String, Func1<CustomerInfoVO, ?>>> getTitleList() {
        List<Pair<String, Func1<CustomerInfoVO, ?>>> list = new ArrayList<>();
        list.add(Pair.of("企业名称", CustomerInfoVO::getName));
        list.add(Pair.of("所在地省代码", CustomerInfoVO::getProvincesCode));
        list.add(Pair.of("所在地省名称", CustomerInfoVO::getProvincesName));
        list.add(Pair.of("所在市代码", CustomerInfoVO::getCityCode));
        list.add(Pair.of("所在市名称", CustomerInfoVO::getCityName));
        list.add(Pair.of("联系人", CustomerInfoVO::getContactPerson));
        list.add(Pair.of("客户类型", CustomerInfoVO::getCustomerTypeName));
        list.add(Pair.of("备注", CustomerInfoVO::getRemark));
        list.add(Pair.of("创建人", CustomerInfoVO::getCreatedName));
        list.add(Pair.of("创建时间", CustomerInfoVO::getCreatedTime));
        list.add(Pair.of("更新人", CustomerInfoVO::getUpdatedName));
        list.add(Pair.of("更新时间", CustomerInfoVO::getUpdatedTime));
        list.add(Pair.of("状态", CustomerInfoVO::getInactiveName));
        return list;
    }

    private CustomerInfoVO convertToVO(CustomerInfoEntity item){
        CustomerInfoVO vo = new CustomerInfoVO();
        vo.setId(item.getId());
        vo.setName(item.getName());
        vo.setProvincesCode(item.getProvincesCode());
        vo.setCityCode(item.getCityCode());
        vo.setContactPerson(item.getContactPerson());
        vo.setPhoneNumber(item.getPhoneNumber());
        vo.setCustomerType(item.getCustomerType());
        vo.setRemark(item.getRemark());
        vo.setCreatedBy(item.getCreatedBy());
        vo.setCreatedTime(item.getCreatedTime());
        vo.setUpdatedBy(item.getUpdatedBy());
        vo.setUpdatedTime(item.getUpdatedTime());
        vo.setInactive(item.getInactive());
        return vo;
    }

    private void setUpdateFieldValue(CustomerInfoUpdateDTO item, CustomerInfoEntity entity){
        entity.setId(item.getId());
        entity.setName(item.getName());
        entity.setProvincesCode(item.getProvincesCode());
        entity.setCityCode(item.getCityCode());
        entity.setContactPerson(item.getContactPerson());
        entity.setPhoneNumber(item.getPhoneNumber());
        entity.setCustomerType(item.getCustomerType());
        entity.setRemark(item.getRemark());
        entity.setInactive(item.getInactive());
    }

    private CustomerInfoEntity convertToEntity(CustomerInfoAddDTO item){
            CustomerInfoEntity entity = new CustomerInfoEntity();
        entity.setName(item.getName());
        entity.setProvincesCode(item.getProvincesCode());
        entity.setCityCode(item.getCityCode());
        entity.setContactPerson(item.getContactPerson());
        entity.setPhoneNumber(item.getPhoneNumber());
        entity.setCustomerType(item.getCustomerType());
        entity.setRemark(item.getRemark());
        entity.setInactive(item.getInactive());
        entity.setResponsibleUserId(item.getResponsibleUserId());
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(CustomerInfoAddDTO dto) {
        CustomerInfoEntity entity = this.convertToEntity(dto);
        baseRepository.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    public void update(CustomerInfoUpdateDTO dto) {
        Integer id = dto.getId();
        CustomerInfoEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.setUpdateFieldValue(dto, entity);
        baseRepository.updateById(entity);
    }
    @Override
    public CustomerInfoVO detail(Integer id) {
        CustomerInfoEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        return this.convertToVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<Integer> idList) {
       this.delete(idList.get(0));
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        CustomerInfoEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        boolean exist = certificateRequirementsService.existByCustomerId(id);
        if (exist) {
            super.throwDataExistAssociated(id);
        }
        baseRepository.removeById(id);
    }
}
