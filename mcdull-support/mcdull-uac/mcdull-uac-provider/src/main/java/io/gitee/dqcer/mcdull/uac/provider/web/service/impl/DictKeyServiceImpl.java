package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.DictKeyAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictKeyEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictKeyVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDictKeyRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDictKeyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
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
        extends BasicServiceImpl<IDictKeyRepository>  implements IDictKeyService {

    @Resource
    private IAuditManager auditManager;

    @Transactional(readOnly = true)
    @Override
    public List<DictKeyVO> queryAll() {
        List<DictKeyEntity> entityList = baseRepository.getListAll();
        if (CollUtil.isNotEmpty(entityList)) {
            return entityList.stream().map(this::buildVO).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    @Override
    public PagedVO<DictKeyVO> getList(DictKeyQueryDTO dto) {
        Page<DictKeyEntity> entityPage = baseRepository.selectPage(dto);
        List<DictKeyVO> voList = new ArrayList<>();
        entityPage.getRecords().forEach(entity -> voList.add(this.buildVO(entity)));
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(DictKeyAddDTO dto) {
        List<DictKeyEntity> listAll = baseRepository.getListAll();
        String keyName = dto.getKeyName();
        String keyCode = dto.getKeyCode();
        if (CollUtil.isNotEmpty(listAll)) {
            this.validNameExist(null, keyName, listAll, entity -> entity.getKeyName().equals(keyName));
            this.validNameExist(null, keyCode, listAll, entity -> entity.getKeyCode().equals(keyCode));
        }
        DictKeyEntity entity = baseRepository.insert(keyCode, keyName, dto.getRemark());
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
        List<DictKeyEntity> list = baseRepository.listByIds(idList);
        if (CollUtil.isEmpty(list) || !NumberUtil.equals(idList.size(), list.size())) {
            this.throwDataNotExistException(StrUtil.join(StrUtil.COMMA, idList));
        }
        baseRepository.removeByIds(idList);
        for (DictKeyEntity entity : list) {
            auditManager.saveByDeleteEnum(entity.getKeyName(), entity.getId(), null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(DictKeyUpdateDTO dto) {
        Integer dictKeyId = dto.getDictKeyId();
        DictKeyEntity dictKey = baseRepository.getById(dictKeyId);
        if (ObjUtil.isNull(dictKey)) {
            this.throwDataNotExistException(dictKeyId);
        }
        String keyCode = dto.getKeyCode();
        String keyName = dto.getKeyName();
        List<DictKeyEntity> listAll = baseRepository.getListAll();
        if (CollUtil.isNotEmpty(listAll)) {
            this.validNameExist(dictKeyId, keyCode, listAll,
                    entity -> (!entity.getId().equals(dictKeyId)) && (entity.getKeyCode().equals(keyCode)));
            this.validNameExist(dictKeyId, keyName, listAll,
                    entity -> (!entity.getId().equals(dictKeyId)) && (entity.getKeyName().equals(keyName)));
            baseRepository.update(dictKeyId, keyCode, keyName, dto.getRemark());
            auditManager.saveByUpdateEnum(keyName, dictKeyId,
                    this.buildAuditLog(dictKey), this.buildAuditLog(baseRepository.getById(dictKeyId)));
        }
    }

    @Override
    public DictKeyVO  getByCode(String keyCode) {
        List<DictKeyEntity> listAll = baseRepository.getListAll();
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
        return baseRepository.getById(keyId);
    }

    private DictKeyVO buildVO(DictKeyEntity entity) {
        DictKeyVO vo = new DictKeyVO();
        vo.setDictKeyId(entity.getId());
        vo.setKeyCode(entity.getKeyCode());
        vo.setKeyName(entity.getKeyName());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}
