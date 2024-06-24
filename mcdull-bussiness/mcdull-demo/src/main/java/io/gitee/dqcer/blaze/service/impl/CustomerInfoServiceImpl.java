package io.gitee.dqcer.blaze.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.blaze.dao.repository.ICustomerInfoRepository;
import io.gitee.dqcer.blaze.domain.entity.CustomerInfoEntity;
import io.gitee.dqcer.blaze.domain.form.CustomerInfoAddDTO;
import io.gitee.dqcer.blaze.domain.form.CustomerInfoQueryDTO;
import io.gitee.dqcer.blaze.domain.form.CustomerInfoUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.CustomerInfoVO;
import io.gitee.dqcer.blaze.service.ICustomerInfoService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业信息 Service
 *
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */

@Service
public class CustomerInfoServiceImpl
        extends BasicServiceImpl<ICustomerInfoRepository> implements ICustomerInfoService {

    public PagedVO<CustomerInfoVO> queryPage(CustomerInfoQueryDTO dto) {
        List<CustomerInfoVO> voList = new ArrayList<>();
        Page<CustomerInfoEntity> entityPage = baseRepository.selectPage(dto);
        List<CustomerInfoEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            for (CustomerInfoEntity entity : recordList) {
                CustomerInfoVO vo = this.convertToVO(entity);
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
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
        baseRepository.removeById(id);
    }
}
