package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.DictValueAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictKeyEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictValueEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictKeyVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictValueVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDictValueRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDictKeyService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDictValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
        extends BasicServiceImpl<IDictValueRepository> implements IDictValueService {

    @Resource
    private IDictKeyService dictKeyService;

    @Resource
    private IAuditManager auditManager;

    @Override
    public PagedVO<DictValueVO> getList(DictValueQueryDTO dto) {
        Page<DictValueEntity> entityPage = baseRepository.selectPage(dto);
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
        List<DictValueEntity> entityList = baseRepository.getListByDictKeyId(dto.getDictKeyId());
        String valueName = dto.getValueName();
        String valueCode = dto.getValueCode();
        this.validNameExist(null, valueName, entityList, i -> i.getValueName().equals(valueName));
        this.validNameExist(null, valueCode, entityList, i -> i.getValueCode().equals(valueCode));
        DictValueEntity entity = baseRepository.insert(dto);
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
        DictValueEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            super.throwDataNotExistException(id);
        }
        String valueName = dto.getValueName();
        String valueCode = dto.getValueCode();
        List<DictValueEntity> entityList = baseRepository.getListByDictKeyId(dto.getDictKeyId());
        this.validNameExist(id, valueName, entityList, i -> (!i.getId().equals(id)) && i.getValueName().equals(valueName));
        this.validNameExist(id, valueCode, entityList, i -> (!i.getId().equals(id)) && i.getValueCode().equals(valueCode));
        baseRepository.update(dto);
        auditManager.saveByUpdateEnum(valueName, id,
                this.buildAuditLog(entity), this.buildAuditLog(baseRepository.getById(id)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> idList) {
        DictValueEntity entity = baseRepository.getById(idList.get(0));
        if (ObjUtil.isNotNull(entity)) {
            List<DictValueEntity> list = baseRepository.getListByDictKeyId(entity.getDictKeyId());
            if (CollUtil.isNotEmpty(list)) {
                List<Integer> collect = list.stream().map(IdEntity::getId).collect(Collectors.toList());
                if (!CollUtil.containsAll(collect, idList)) {
                    throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
                }
                baseRepository.removeByIds(idList);
                for (Integer id : idList) {
                    auditManager.saveByDeleteEnum(entity.getValueName(), id, null);
                }
            }
        }
    }

    @Override
    public List<DictValueVO> selectByKeyCode(String keyCode) {
        DictKeyVO vo = dictKeyService.getByCode(keyCode);
        if (ObjUtil.isNotNull(vo)) {
            return baseRepository.getListByDictKeyId(vo.getDictKeyId()).stream().map(i -> {
                DictValueVO valueVO = new DictValueVO();
                valueVO.setDictValueId(i.getId());
                valueVO.setValueCode(i.getValueCode());
                valueVO.setValueName(i.getValueName());
                return valueVO;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
