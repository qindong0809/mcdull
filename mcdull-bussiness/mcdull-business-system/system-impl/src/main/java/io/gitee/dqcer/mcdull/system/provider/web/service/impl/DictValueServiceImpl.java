package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.audit.DictValueAudit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DictValueAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DictValueQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DictValueUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DictKeyEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DictValueEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DictKeyVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DictValueVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.DictValueMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDictKeyService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDictValueService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dict Value Service Impl
 *
 * @author dqcer
 * @since 2024/7/25 10:23
 */

@Service
public class DictValueServiceImpl
        extends BasicCurdServiceImpl<DictValueMapper, DictValueEntity> implements IDictValueService {

    @Resource
    private IDictKeyService dictKeyService;

    @Resource
    private IAuditManager auditManager;

    @Override
    public PagedVO<DictValueVO> getList(DictValueQueryDTO dto) {
        Page<DictValueEntity> entityPage = this.selectPage(dto);
        List<DictValueVO> voList = new ArrayList<>();
        entityPage.getRecords().forEach(entity -> {
            DictValueVO vo = new DictValueVO();
            vo.setDictValueId(entity.getId());
            vo.setDictKeyId(entity.getDictKeyId());
            vo.setValueCode(entity.getValueCode());
            vo.setValueName(entity.getValueName());
            vo.setSort(entity.getSort());
            vo.setRemark(entity.getRemark());
            voList.add(vo);
        });
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(DictValueAddDTO dto) {
        List<DictValueEntity> entityList = this.getListByDictKeyId(dto.getDictKeyId());
        String valueName = dto.getValueName();
        String valueCode = dto.getValueCode();
        LogicCheckUtil.validNameExist(null, valueName, entityList, i -> i.getValueName().equals(valueName));
        LogicCheckUtil.validNameExist(null, valueCode, entityList, i -> i.getValueCode().equals(valueCode));
        DictValueEntity entity = this.insertEntity(dto);
//        auditManager.saveByAddEnum(entity.getValueName(), entity.getId(), this.buildAuditLog(entity));
    }

    private Audit buildAuditLog(DictValueEntity entity) {
        DictValueAudit audit = new DictValueAudit();
        audit.setValueName(entity.getValueName());
        audit.setValueCode(entity.getValueCode());
        Integer dictKeyId = entity.getDictKeyId();
        if (ObjUtil.isNotNull(dictKeyId)) {
            DictKeyEntity dictKey = dictKeyService.getById(dictKeyId);
            if (ObjUtil.isNotNull(dictKey)) {
                audit.setKeyName(dictKey.getKeyName());
            }
        }
        audit.setRemark(entity.getRemark());
        audit.setSort(entity.getSort());
        return audit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(DictValueUpdateDTO dto) {
        Integer id = dto.getDictValueId();
        DictValueEntity entity = super.getById(id);
        if (ObjUtil.isNull(entity)) {
            LogicCheckUtil.throwDataNotExistException(id);
        }
        String valueName = dto.getValueName();
        String valueCode = dto.getValueCode();
        List<DictValueEntity> entityList = this.getListByDictKeyId(dto.getDictKeyId());
        LogicCheckUtil.validNameExist(id, valueName, entityList, i -> (!i.getId().equals(id)) && i.getValueName().equals(valueName));
        LogicCheckUtil.validNameExist(id, valueCode, entityList, i -> (!i.getId().equals(id)) && i.getValueCode().equals(valueCode));
        this.updateEntity(dto);
        auditManager.saveByUpdateEnum(valueName, id,
                this.buildAuditLog(entity), this.buildAuditLog(super.getById(id)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> idList) {
        DictValueEntity entity = super.getById(idList.get(0));
        if (ObjUtil.isNull(entity)) {
            LogicCheckUtil.throwDataNotExistException(idList.get(0));
        }
        List<DictValueEntity> list = this.getListByDictKeyId(entity.getDictKeyId());
        if (CollUtil.isNotEmpty(list)) {
            List<Integer> collect = list.stream().map(IdEntity::getId).collect(Collectors.toList());
            if (!CollUtil.containsAll(collect, idList)) {
                throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
            }
            super.removeByIds(idList);
            for (Integer id : idList) {
                auditManager.saveByDeleteEnum(entity.getValueName(), id, null);
            }
        }
    }

    @Override
    public List<DictValueVO> selectByKeyCode(String keyCode) {
        DictKeyVO vo = dictKeyService.getByCode(keyCode);
        if (ObjUtil.isNotNull(vo)) {
            return this.getListByDictKeyId(vo.getDictKeyId()).stream().map(i -> {
                DictValueVO valueVO = new DictValueVO();
                valueVO.setDictValueId(i.getId());
                valueVO.setValueCode(i.getValueCode());
                valueVO.setValueName(i.getValueName());
                return valueVO;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public void insert(DictValueEntity entity) {
        baseMapper.insert(entity);
    }


    public List<DictValueEntity> getListByDictKeyId(Integer dictKeyId) {
        if (ObjectUtil.isNotNull(dictKeyId)) {
            LambdaQueryWrapper<DictValueEntity> wrapper = Wrappers.lambdaQuery(DictValueEntity.class)
                .eq(DictValueEntity::getDictKeyId, dictKeyId);
            return baseMapper.selectList(wrapper);
        }
        return Collections.emptyList();
    }

    public Page<DictValueEntity> selectPage(DictValueQueryDTO dto) {
        LambdaQueryWrapper<DictValueEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getSearchWord();
        if (CharSequenceUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(DictValueEntity::getValueName, keyword)
                .or().like(DictValueEntity::getValueCode, keyword)
            );
        }
        Integer dictKeyId = dto.getDictKeyId();
        if (ObjectUtil.isNotNull(dictKeyId)) {
            query.eq(DictValueEntity::getDictKeyId, dictKeyId);
        }
        query.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    public DictValueEntity insertEntity(DictValueAddDTO dto) {
        DictValueEntity entity = new DictValueEntity();
        entity.setDictKeyId(dto.getDictKeyId());
        entity.setValueCode(dto.getValueCode());
        entity.setValueName(dto.getValueName());
        entity.setSort(dto.getSort());
        entity.setRemark(dto.getRemark());
        this.insert(entity);
        return entity;
    }

    public void updateEntity(DictValueUpdateDTO dto) {
        DictValueEntity entity = new DictValueEntity();
        entity.setId(dto.getDictValueId());
        entity.setValueCode(dto.getValueCode());
        entity.setValueName(dto.getValueName());
        entity.setSort(dto.getSort());
        entity.setRemark(dto.getRemark());
        this.updateById(entity);
    }
}
