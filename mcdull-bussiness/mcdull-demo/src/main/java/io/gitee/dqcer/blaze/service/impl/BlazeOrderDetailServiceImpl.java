package io.gitee.dqcer.blaze.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.blaze.dao.repository.IBlazeOrderDetailRepository;
import io.gitee.dqcer.blaze.domain.entity.BlazeOrderDetailEntity;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderDetailAddDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderDetailQueryDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderDetailUpdateDTO;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderQueryDTO;
import io.gitee.dqcer.blaze.domain.vo.BlazeOrderDetailListVO;
import io.gitee.dqcer.blaze.domain.vo.BlazeOrderDetailVO;
import io.gitee.dqcer.blaze.domain.vo.BlazeOrderVO;
import io.gitee.dqcer.blaze.service.IBlazeOrderDetailService;
import io.gitee.dqcer.blaze.service.IBlazeOrderService;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.InactiveEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@Service
public class BlazeOrderDetailServiceImpl
        extends BasicServiceImpl<IBlazeOrderDetailRepository> implements IBlazeOrderDetailService {

    @Resource
    private IBlazeOrderService blazeOrderService;
    @Resource
    private IUserManager userManager;

    @Override
    public PagedVO<BlazeOrderDetailVO> queryPage(BlazeOrderDetailQueryDTO dto) {
        List<BlazeOrderDetailVO> voList = new ArrayList<>();
        Page<BlazeOrderDetailEntity> entityPage = baseRepository.selectPage(dto);
        for (BlazeOrderDetailEntity entity : entityPage.getRecords()) {
            BlazeOrderDetailVO vo = this.convertToVO(entity);
            voList.add(vo);
        }
        return PageUtil.toPage(voList, entityPage);
    }

    private BlazeOrderDetailVO convertToVO(BlazeOrderDetailEntity entity) {
        BlazeOrderDetailVO vo = new BlazeOrderDetailVO();
        vo.setId(entity.getId());
        vo.setBlazeOrderId(entity.getBlazeOrderId());
        vo.setIsTalent(entity.getIsTalent());
        vo.setResponsibleUserId(entity.getResponsibleUserId());
        vo.setOperationTime(entity.getOperationTime());
        vo.setPrice(entity.getPrice().toString());
        vo.setRemarks(entity.getRemarks());
        vo.setCreatedBy(entity.getCreatedBy());
        vo.setCreatedTime(entity.getCreatedTime());
        vo.setUpdatedBy(entity.getUpdatedBy());
        vo.setUpdatedTime(entity.getUpdatedTime());
        vo.setInactive(entity.getInactive());
        vo.setInactiveStr(IEnum.getTextByCode(InactiveEnum.class, entity.getInactive()));
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void exportData(BlazeOrderDetailQueryDTO dto) {

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(BlazeOrderDetailAddDTO dto) {
        BlazeOrderDetailEntity entity = this.convertToEntity(dto);
        baseRepository.save(entity);
    }

    private BlazeOrderDetailEntity convertToEntity(BlazeOrderDetailAddDTO dto) {
        BlazeOrderDetailEntity entity = new BlazeOrderDetailEntity();
        entity.setBlazeOrderId(dto.getBlazeOrderId());
        entity.setIsTalent(dto.getIsTalent());
        entity.setResponsibleUserId(dto.getResponsibleUserId());
        entity.setOperationTime(dto.getOperationTime());
        entity.setPrice(dto.getPrice());
        entity.setRemarks(dto.getRemarks());
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(BlazeOrderDetailUpdateDTO dto) {
        Integer id = dto.getId();
        BlazeOrderDetailEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.settingUpdateValue(dto, entity);
        baseRepository.updateById(entity);
    }

    private void settingUpdateValue(BlazeOrderDetailUpdateDTO dto, BlazeOrderDetailEntity entity) {
        entity.setResponsibleUserId(dto.getResponsibleUserId());
        entity.setOperationTime(dto.getOperationTime());
        entity.setPrice(dto.getPrice());
        entity.setRemarks(dto.getRemarks());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        BlazeOrderDetailEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        baseRepository.removeById(id);
    }

    @Override
    public List<BlazeOrderDetailListVO> getOderList() {
        BlazeOrderQueryDTO dto = new BlazeOrderQueryDTO();
        PageUtil.setMaxPageSize(dto);
        // todo 状态过滤，打款完成过滤
        PagedVO<BlazeOrderVO> page = blazeOrderService.queryPage(dto);
        if (CollUtil.isNotEmpty(page.getList())) {
            List<BlazeOrderDetailListVO> voList = new ArrayList<>();
            List<BlazeOrderVO> list = page.getList();
            for (BlazeOrderVO blazeOrderVO : list) {
                BlazeOrderDetailListVO vo = new BlazeOrderDetailListVO();
                vo.setBlazeOrderId(blazeOrderVO.getId());
                vo.setOrderNo(blazeOrderVO.getOrderNo());
                vo.setTalentCertTitle(blazeOrderVO.getTalentCertName());
                vo.setCustomerCertTitle(vo.getCustomerCertTitle());
                voList.add(vo);
            }
            return voList;
        }
        return List.of();
    }

    @Override
    public List<LabelValueVO<Integer, String>> getResponsibleList() {
        return userManager.getResponsibleList();
    }
}
