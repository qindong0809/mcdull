package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.HelpDocCatalogAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocCatalogAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocCatalogUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocCatalogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocCatalogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocCatalogRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IHelpDocCatalogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Help Doc Catalog Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class HelpDocCatalogServiceImpl
        extends BasicServiceImpl<IHelpDocCatalogRepository> implements IHelpDocCatalogService {

    @Resource
    private IAuditManager auditManager;

    @Override
    public List<HelpDocCatalogVO> getAll() {
        List<HelpDocCatalogEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            List<HelpDocCatalogVO> voList = new ArrayList<>();
            for (HelpDocCatalogEntity entity : list) {
                voList.add(this.convert(entity));
            }
            return voList;
        }
        return Collections.emptyList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(HelpDocCatalogAddDTO dto) {
        List<HelpDocCatalogEntity> list = baseRepository.list(dto.getParentId());
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(null, dto.getName(), list,
                    entity -> entity.getName().equals(dto.getName()));
        }
        HelpDocCatalogEntity entity = new HelpDocCatalogEntity();
        entity.setName(dto.getName());
        entity.setSort(dto.getSort());
        entity.setParentId(dto.getParentId());
        baseRepository.insert(entity);

        auditManager.saveByAddEnum(dto.getName(), entity.getId(), this.buildAuditLog(entity));
    }

    private Audit buildAuditLog(HelpDocCatalogEntity entity) {
        HelpDocCatalogAudit audit = new HelpDocCatalogAudit();
        audit.setName(entity.getName());
        Integer parentId = entity.getParentId();
        if (ObjUtil.isNotNull(parentId)) {
            HelpDocCatalogEntity parentEntity = baseRepository.getById(parentId);
            if (ObjUtil.isNotNull(parentEntity)) {
                audit.setParentName(parentEntity.getName());
            }
        }
        audit.setSort(entity.getSort());
        return audit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(HelpDocCatalogUpdateDTO dto) {
        Integer helpDocCatalogId = dto.getHelpDocCatalogId();
        HelpDocCatalogEntity helpDocCatalog = baseRepository.getById(helpDocCatalogId);
        if (ObjUtil.isNull(helpDocCatalog)) {
            this.throwDataNotExistException(helpDocCatalogId);
        }
        HelpDocCatalogEntity oldEntity = ObjUtil.cloneByStream(helpDocCatalog);
        List<HelpDocCatalogEntity> list = baseRepository.list(dto.getParentId());
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(helpDocCatalogId, dto.getName(), list,
                    entity -> (!helpDocCatalogId.equals(entity.getId())) && entity.getName().equals(dto.getName()));
        }
        helpDocCatalog.setName(dto.getName());
        helpDocCatalog.setSort(dto.getSort());
        baseRepository.updateById(helpDocCatalog);
        auditManager.saveByUpdateEnum(dto.getName(), helpDocCatalogId,
                this.buildAuditLog(oldEntity), this.buildAuditLog(helpDocCatalog));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        HelpDocCatalogEntity helpDocCatalog = baseRepository.getById(id);
        if (ObjUtil.isNull(helpDocCatalog)) {
            this.throwDataNotExistException(id);
        }
        baseRepository.removeById(helpDocCatalog);
        auditManager.saveByDeleteEnum(helpDocCatalog.getName(), id, null);
    }

    @Override
    public HelpDocCatalogEntity getById(Integer id) {
        return baseRepository.getById(id);
    }

    @Override
    public List<HelpDocCatalogEntity> queryListByIds(List<Integer> idList) {
        if (CollUtil.isNotEmpty(idList)) {
            return baseRepository.queryListByIds(idList);
        }
        return Collections.emptyList();
    }

    private HelpDocCatalogVO convert(HelpDocCatalogEntity entity) {
        HelpDocCatalogVO helpDocCatalogVO = new HelpDocCatalogVO();
        helpDocCatalogVO.setHelpDocCatalogId(Convert.toInt(entity.getId()));
        helpDocCatalogVO.setName(entity.getName());
        helpDocCatalogVO.setParentId(Convert.toInt(entity.getParentId()));
        helpDocCatalogVO.setSort(entity.getSort());
        return helpDocCatalogVO;
    }
}
