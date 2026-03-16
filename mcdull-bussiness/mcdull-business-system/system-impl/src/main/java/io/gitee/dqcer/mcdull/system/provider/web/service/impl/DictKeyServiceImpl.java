package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.audit.DictKeyAudit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DictKeyAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DictKeyQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DictKeyUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DictKeyEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DictKeyVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.DictKeyMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDictKeyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dict Key Service Impl
 *
 * @author dqcer
 * @since 2024/7/25 10:24
 */
@Service
public class DictKeyServiceImpl
        extends BasicCurdServiceImpl<DictKeyMapper, DictKeyEntity> implements IDictKeyService {

    @Resource
    private IAuditManager auditManager;
    @Resource
    private ICommonManager commonManager;

    @Transactional(readOnly = true)
    @Override
    public List<DictKeyVO> queryAll() {
        List<DictKeyEntity> entityList = this.getListAll();
        if (CollUtil.isNotEmpty(entityList)) {
            return entityList.stream().map(this::buildVO).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    @Override
    public PagedVO<DictKeyVO> queryPage(DictKeyQueryDTO dto) {
        Page<DictKeyEntity> entityPage = this.selectPage(dto);
        List<DictKeyVO> voList = new ArrayList<>();
        entityPage.getRecords().forEach(entity -> voList.add(this.buildVO(entity)));
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(DictKeyAddDTO dto) {
        List<DictKeyEntity> listAll = this.getListAll();
        String keyName = dto.getKeyName();
        String keyCode = dto.getKeyCode();
        if (CollUtil.isNotEmpty(listAll)) {
            LogicCheckUtil.validNameExist(null, keyName, listAll, entity -> entity.getKeyName().equals(keyName));
            LogicCheckUtil.validNameExist(null, keyCode, listAll, entity -> entity.getKeyCode().equals(keyCode));
        }
        DictKeyEntity entity = this.insert(keyCode, keyName, dto.getRemark());
        auditManager.saveByAddEnum(entity.getKeyName(), entity.getId(), this.buildAuditLog(entity));
    }

    private Audit buildAuditLog(DictKeyEntity entity) {
        DictKeyAudit audit = new DictKeyAudit();
        audit.setKeyName(entity.getKeyName());
        audit.setKeyCode(entity.getKeyCode());
        audit.setRemark(entity.getRemark());
        return audit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> idList) {
        List<DictKeyEntity> list = this.listByIds(idList);
        if (CollUtil.isEmpty(list) || !NumberUtil.equals(idList.size(), list.size())) {
            LogicCheckUtil.throwDataNotExistException(StrUtil.join(StrUtil.COMMA, idList));
        }
        super.removeByIds(idList);
        for (DictKeyEntity entity : list) {
            auditManager.saveByDeleteEnum(entity.getKeyName(), entity.getId(), null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(DictKeyUpdateDTO dto) {
        Integer dictKeyId = dto.getDictKeyId();
        DictKeyEntity dictKey = super.getById(dictKeyId);
        if (ObjUtil.isNull(dictKey)) {
            LogicCheckUtil.throwDataNotExistException(dictKeyId);
        }
        String keyCode = dto.getKeyCode();
        String keyName = dto.getKeyName();
        List<DictKeyEntity> listAll = this.getListAll();
        if (CollUtil.isNotEmpty(listAll)) {
            LogicCheckUtil.validNameExist(dictKeyId, keyCode, listAll,
                    entity -> (!entity.getId().equals(dictKeyId)) && (entity.getKeyCode().equals(keyCode)));
            LogicCheckUtil.validNameExist(dictKeyId, keyName, listAll,
                    entity -> (!entity.getId().equals(dictKeyId)) && (entity.getKeyName().equals(keyName)));
            this.update(dictKeyId, keyCode, keyName, dto.getRemark());
            auditManager.saveByUpdateEnum(keyName, dictKeyId,
                    this.buildAuditLog(dictKey), this.buildAuditLog(super.getById(dictKeyId)));
        }
    }

    @Override
    public DictKeyVO  getByCode(String keyCode) {
        List<DictKeyEntity> listAll = this.getListAll();
        if (CollUtil.isNotEmpty(listAll)) {
            for (DictKeyEntity entity : listAll) {
                if (entity.getKeyCode().equals(keyCode)) {
                    return this.buildVO(entity);
                }
            }
        }
        return null;
    }

    @Override
    public DictKeyEntity getById(Integer keyId) {
        return this.getById(keyId);
    }

    @Override
    public boolean exportData(DictKeyQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitleList());
        return true;
    }

    private List<Pair<String, Func1<DictKeyVO, ?>>> getTitleList() {
        return ListUtil.of(
                Pair.of("字典编码", DictKeyVO::getKeyCode),
                Pair.of("字典名称", DictKeyVO::getKeyName),
                Pair.of("字典描述", DictKeyVO::getRemark)
        );
    }


    private DictKeyVO buildVO(DictKeyEntity entity) {
        DictKeyVO vo = new DictKeyVO();
        vo.setDictKeyId(entity.getId());
        vo.setKeyCode(entity.getKeyCode());
        vo.setKeyName(entity.getKeyName());
        vo.setRemark(entity.getRemark());
        return vo;
    }

    public void insert(DictKeyEntity entity) {
        baseMapper.insert(entity);
    }


    public List<DictKeyEntity> getListAll() {
        return baseMapper.selectList(null);
    }

    public Page<DictKeyEntity> selectPage(DictKeyQueryDTO dto) {
        LambdaQueryWrapper<DictKeyEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getSearchWord();
        if (CharSequenceUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(DictKeyEntity::getKeyName, keyword)
                .or().like(DictKeyEntity::getKeyCode, keyword)
            );
        }
        query.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    public DictKeyEntity insert(String keyCode, String keyName, String remark) {
        if (CharSequenceUtil.isBlank(keyName) || CharSequenceUtil.isBlank(keyCode)) {
            throw new IllegalArgumentException("keyName or keyCode is null");
        }
        DictKeyEntity entity = new DictKeyEntity();
        entity.setKeyCode(keyCode);
        entity.setKeyName(keyName);
        entity.setRemark(remark);
        this.insert(entity);
        return entity;
    }

    public void update(Integer dictKeyId, String keyCode, String keyName, String remark) {
        DictKeyEntity entity = new DictKeyEntity();
        entity.setId(dictKeyId);
        entity.setKeyCode(keyCode);
        entity.setKeyName(keyName);
        entity.setRemark(remark);
        this.updateById(entity);
    }
}
