package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.TimeZoneUtil;
import io.gitee.dqcer.mcdull.framework.web.version.IVersionInfoComponent;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.ChangeLogAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ChangeLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ChangeLogAndVersionVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ChangeLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IChangeLogRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IChangeLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Change Log Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class ChangeLogServiceImpl
        extends BasicServiceImpl<IChangeLogRepository> implements IChangeLogService {

    @Resource
    private IVersionInfoComponent versionInfoComponent;

    @Resource
    private IAuditManager auditManager;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(ChangeLogAddDTO dto) {
        List<ChangeLogEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(null, dto.getVersion(), list,
                    entity -> entity.getVersion().equals(dto.getVersion()));
        }
        ChangeLogEntity entity = this.convertEntity(dto);
        baseRepository.insert(entity);
        auditManager.saveByAddEnum(dto.getVersion(), entity.getId(), this.buildAuditLog(entity));
    }

    private Audit buildAuditLog(ChangeLogEntity entity) {
        ChangeLogAudit audit = new ChangeLogAudit();
        audit.setVersion(entity.getVersion());
        audit.setContent(entity.getContent());
        audit.setPublicDate(TimeZoneUtil.serializeDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        audit.setPublishAuthor(entity.getPublishAuthor());
        audit.setLink(entity.getLink());
//        audit.setType(entity.getType());
        return audit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ChangeLogUpdateDTO dto) {
        Integer changeLogId = dto.getChangeLogId();
        ChangeLogEntity logEntity = baseRepository.getById(changeLogId);
        if (ObjUtil.isNull(logEntity)) {
            this.throwDataNotExistException(changeLogId);
        }
        List<ChangeLogEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(changeLogId, dto.getVersion(), list,
                    entity -> (!changeLogId.equals(entity.getId())) && entity.getVersion().equals(dto.getVersion()));
        }
        this.settingUpdateField(dto, logEntity);
        baseRepository.updateById(logEntity);
        auditManager.saveByUpdateEnum(dto.getVersion(), changeLogId,
                this.buildAuditLog(logEntity), this.buildAuditLog(baseRepository.getById(changeLogId)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<Integer> idList) {
        List<ChangeLogEntity> entityList = baseRepository.queryListByIds(idList);
        if (entityList.size() != idList.size()) {
            this.throwDataNotExistException(idList);
        }
        baseRepository.removeByIds(idList);
        for (ChangeLogEntity entity : entityList) {
            auditManager.saveByDeleteEnum(entity.getVersion(), entity.getId(), null);
        }
    }

    @Override
    public PagedVO<ChangeLogVO> queryPage(ChangeLogQueryDTO dto) {
        Page<ChangeLogEntity> entityPage = baseRepository.selectPage(dto);
        List<ChangeLogVO> voList = new ArrayList<>();
        for (ChangeLogEntity entity : entityPage.getRecords()) {
            voList.add(this.convertToConfigVO(entity));
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public ChangeLogVO getById(Integer id) {
        ChangeLogEntity logEntity = baseRepository.getById(id);
        if (ObjUtil.isNull(logEntity)) {
            this.throwDataNotExistException(id);
        }
        return this.convertToConfigVO(logEntity);
    }

    @Override
    public ChangeLogAndVersionVO getChangeLogAndVersion() {
        ChangeLogAndVersionVO changeLogAndVersion = new ChangeLogAndVersionVO();
        List<ChangeLogEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            List<ChangeLogVO> voList = new ArrayList<>();
            for (ChangeLogEntity entity : list) {
                voList.add(this.convertToConfigVO(entity));
            }
            voList.sort((o1, o2) -> o2.getPublicDate().compareTo(o1.getPublicDate()));
            changeLogAndVersion.setList(voList);
        }
        changeLogAndVersion.setGitVersion(versionInfoComponent.getGitCurrentCommitInfo());
        changeLogAndVersion.setJarVersion(versionInfoComponent.getJarCurrentBuildInfo());
        return changeLogAndVersion;
    }

    private ChangeLogVO convertToConfigVO(ChangeLogEntity entity) {
        ChangeLogVO changeLogVO = new ChangeLogVO();
        changeLogVO.setChangeLogId(entity.getId());
        changeLogVO.setVersion(entity.getVersion());
        changeLogVO.setType(entity.getType());
        changeLogVO.setPublishAuthor(entity.getPublishAuthor());
        changeLogVO.setPublicDate(entity.getPublicDate());
        changeLogVO.setContent(entity.getContent());
        changeLogVO.setLink(entity.getLink());
        changeLogVO.setCreateTime(entity.getCreatedTime());
        changeLogVO.setUpdateTime(entity.getUpdatedTime());
        return changeLogVO;
    }

    private void settingUpdateField(ChangeLogUpdateDTO dto, ChangeLogEntity changeLogEntity) {
        changeLogEntity.setVersion(dto.getVersion());
        changeLogEntity.setType(dto.getType());
        changeLogEntity.setPublishAuthor(dto.getPublishAuthor());
        changeLogEntity.setPublicDate(dto.getPublicDate());
        changeLogEntity.setContent(dto.getContent());
        changeLogEntity.setLink(dto.getLink());
    }

    private ChangeLogEntity convertEntity(ChangeLogAddDTO dto) {
        ChangeLogEntity changeLogEntity = new ChangeLogEntity();
        changeLogEntity.setVersion(dto.getVersion());
        changeLogEntity.setType(dto.getType());
        changeLogEntity.setPublishAuthor(dto.getPublishAuthor());
        changeLogEntity.setPublicDate(dto.getPublicDate());
        changeLogEntity.setContent(dto.getContent());
        changeLogEntity.setLink(dto.getLink());
        return changeLogEntity;
    }
}
