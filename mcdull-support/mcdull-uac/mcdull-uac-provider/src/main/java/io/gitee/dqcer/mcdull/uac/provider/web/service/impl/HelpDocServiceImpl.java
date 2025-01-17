package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.HelpDocAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocViewRecordQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.*;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocDetailVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocViewRecordVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocRelationRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocViewRecordRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IHelpDocCatalogService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IHelpDocService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
* Help Doc ServiceImpl
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class HelpDocServiceImpl
        extends BasicServiceImpl<IHelpDocRepository> implements IHelpDocService {

    @Resource
    private IHelpDocViewRecordRepository helpDocViewRecordRepository;

    @Resource
    private IUserManager userManager;

    @Resource
    private IHelpDocCatalogService helpDocCatalogService;

    @Resource
    private IHelpDocRelationRepository helpDocRelationRepository;

    @Resource
    private IAuditManager auditManager;

    @Override
    public HelpDocDetailVO view(Integer helpDocId) {
        HelpDocEntity entity = baseRepository.getById(helpDocId);
        if (entity != null) {
            return this.convert(entity);
        }
        LogHelp.error(log, "help doc not exist, id: {}", helpDocId);
        return null;
    }

    private  HelpDocDetailVO convert(HelpDocEntity entity) {
        HelpDocDetailVO vo = new HelpDocDetailVO();
        vo.setHelpDocId(Convert.toInt(entity.getId()));
        vo.setTitle(entity.getTitle());
        vo.setContentHtml(entity.getContentHtml());
        vo.setContentText(entity.getContentText());
        vo.setAttachment(entity.getAttachment());
        vo.setPageViewCount(entity.getPageViewCount());
        vo.setUserViewCount(entity.getUserViewCount());
        vo.setAuthor(entity.getAuthor());
        vo.setHelpDocCatalogId(Convert.toInt(entity.getHelpDocCatalogId()));
        vo.setHelpDocCatalogName(helpDocCatalogService.getById(entity.getHelpDocCatalogId()).getName());
        return vo;
    }

    @Override
    public List<HelpDocVO> queryAllHelpDocList() {
        List<HelpDocEntity> list = baseRepository.list();
        if (list != null) {
            return list.stream().map(entity -> {
                HelpDocVO vo = new HelpDocVO();
                vo.setHelpDocId(entity.getId());
                vo.setTitle(entity.getTitle());
                vo.setHelpDocCatalogId(entity.getHelpDocCatalogId());
                vo.setSort(entity.getSort());
                return vo;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public PagedVO<HelpDocViewRecordVO> queryViewRecord(HelpDocViewRecordQueryDTO dto) {
        Page<HelpDocViewRecordEntity> entityPage = helpDocViewRecordRepository.selectPage(dto);
        List<HelpDocViewRecordVO> voList = new ArrayList<>();
        List<HelpDocViewRecordEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Set<Integer> userIdSet = records.stream().map(HelpDocViewRecordEntity::getUserId).collect(Collectors.toSet());
            Map<Integer, UserEntity> userMap = userManager.getEntityMap(new ArrayList<>(userIdSet));
            for (HelpDocViewRecordEntity entity : records) {
                HelpDocViewRecordVO vo = this.convertRecord(entity);
                if (MapUtil.isNotEmpty(userMap)) {
                    UserEntity userEntity = userMap.get(vo.getUserId());
                    if (ObjUtil.isNotNull(userEntity)) {
                        vo.setUserName(userEntity.getActualName());
                    }
                }
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public PagedVO<HelpDocVO> query(HelpDocQueryDTO dto) {
        Page<HelpDocEntity> entityPage = baseRepository.selectPage(dto);
        List<HelpDocVO> voList = new ArrayList<>();
        List<HelpDocEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Set<Integer> catalogIdSet = records.stream().map(HelpDocEntity::getHelpDocCatalogId).collect(Collectors.toSet());
            List<HelpDocCatalogEntity> catalogEntityList = helpDocCatalogService.queryListByIds(new ArrayList<>(catalogIdSet));
            Map<Integer, HelpDocCatalogEntity> catalogEntityMap = new HashMap<>();
            if (CollUtil.isNotEmpty(catalogEntityList)) {
                catalogEntityMap = catalogEntityList.stream().collect(Collectors.toMap(HelpDocCatalogEntity::getId,
                        Function.identity()));
            }

            for (HelpDocEntity entity : records) {
                HelpDocVO helpDocVO = this.convertEntity(entity);
                if (MapUtil.isNotEmpty(catalogEntityMap)) {
                    if (ObjUtil.isNotNull(helpDocVO)) {
                        HelpDocCatalogEntity catalogEntity = catalogEntityMap.get(helpDocVO.getHelpDocCatalogId());
                        if (ObjUtil.isNotNull(catalogEntity)) {
                            helpDocVO.setHelpDocCatalogName(catalogEntity.getName());
                        }
                    }
                }
                voList.add(helpDocVO);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public HelpDocDetailVO getDetail(Integer helpDocId) {
        HelpDocEntity entity = baseRepository.getById(helpDocId);
        if (entity != null) {
            return this.convert(entity);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(HelpDocAddDTO dto) {
        Integer helpDocCatalogId = dto.getHelpDocCatalogId();
        List<HelpDocEntity> list = baseRepository.listByCatalogId(helpDocCatalogId);
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(null, dto.getTitle(), list, entity -> entity.getTitle().equals(dto.getTitle()));
        }
        HelpDocEntity entity = new HelpDocEntity();
        entity.setTitle(dto.getTitle());
        entity.setContentHtml(dto.getContentHtml());
        entity.setContentText(dto.getContentText());
        entity.setHelpDocCatalogId(helpDocCatalogId);
        entity.setAuthor(dto.getAuthor());
        entity.setSort(dto.getSort());
        entity.setPageViewCount(0);
        entity.setUserViewCount(0);
        entity.setAttachment(dto.getAttachment());
        baseRepository.insert(entity);
        auditManager.saveByAddEnum(dto.getTitle(), entity.getId(), this.buildAuditLog(entity));
    }

    private Audit buildAuditLog(HelpDocEntity entity) {
        HelpDocAudit audit = new HelpDocAudit();
        Integer helpDocCatalogId = entity.getHelpDocCatalogId();
        if (ObjUtil.isNotNull(helpDocCatalogId)) {
            HelpDocCatalogEntity docCatalog = helpDocCatalogService.getById(helpDocCatalogId);
            if (ObjUtil.isNotNull(docCatalog)) {
                audit.setHelpDocCatalogName(docCatalog.getName());
            }
        }
        audit.setTitle(entity.getTitle());
        audit.setContentText(entity.getContentText());
        audit.setContentHtml(entity.getContentHtml());
        audit.setAttachment(entity.getAttachment());
        return audit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(HelpDocUpdateDTO dto) {
        Integer helpDocId = dto.getHelpDocId();
        HelpDocEntity docEntity = baseRepository.getById(helpDocId);
        if (ObjUtil.isNull(docEntity)) {
            this.throwDataNotExistException(helpDocId);
        }
        HelpDocEntity oldEntity = ObjUtil.cloneByStream(docEntity);
        Integer helpDocCatalogId = dto.getHelpDocCatalogId();
        List<HelpDocEntity> list = baseRepository.listByCatalogId(helpDocCatalogId);
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(helpDocId, dto.getTitle(), list,
                    entity -> (!helpDocId.equals(entity.getId())) && entity.getTitle().equals(dto.getTitle()));
        }
        docEntity.setTitle(dto.getTitle());
        docEntity.setContentHtml(dto.getContentHtml());
        docEntity.setContentText(dto.getContentText());
        docEntity.setHelpDocCatalogId(helpDocCatalogId);
        docEntity.setAuthor(dto.getAuthor());
        docEntity.setSort(dto.getSort());
        docEntity.setAttachment(dto.getAttachment());
        baseRepository.updateById(docEntity);
        auditManager.saveByUpdateEnum(dto.getTitle(), helpDocId,
                this.buildAuditLog(oldEntity), this.buildAuditLog(docEntity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer helpDocId) {
        HelpDocEntity entity = baseRepository.getById(helpDocId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(helpDocId);
        }
        baseRepository.removeById(helpDocId);
        auditManager.saveByDeleteEnum(entity.getTitle(), helpDocId, "");
    }

    @Override
    public List<HelpDocVO> queryHelpDocByRelationId(Integer relationId) {
        List<HelpDocRelationEntity> list = helpDocRelationRepository.listByRelationId(relationId);
        if (CollUtil.isNotEmpty(list)) {
            Set<Integer> set = list.stream().map(HelpDocRelationEntity::getHelpDocId).collect(Collectors.toSet());
            List<HelpDocEntity> docEntityList = baseRepository.queryListByIds(new ArrayList<>(set));
            if (CollUtil.isNotEmpty(docEntityList)) {
                Map<Integer, HelpDocEntity> entityMap = docEntityList.stream().collect(Collectors.toMap(IdEntity::getId,
                        Function.identity()));
                List<HelpDocVO> voList = new ArrayList<>();
                for (HelpDocRelationEntity relation : list) {
                    HelpDocVO vo = new HelpDocVO();
                    if (MapUtil.isNotEmpty(entityMap)) {
                        HelpDocEntity entity = entityMap.get(relation.getHelpDocId());
                        if (ObjUtil.isNotNull(entity)) {
                            Integer helpDocCatalogId = entity.getHelpDocCatalogId();
                            HelpDocCatalogEntity helpDocCatalog = helpDocCatalogService.getById(helpDocCatalogId);
                            if (ObjUtil.isNotNull(helpDocCatalog)) {
                                vo.setHelpDocCatalogName(helpDocCatalog.getName());
                            }
                            vo.setTitle(entity.getTitle());
                            vo.setAuthor(entity.getAuthor());
                            vo.setSort(entity.getSort());
                            vo.setPageViewCount(entity.getPageViewCount());
                            vo.setUserViewCount(entity.getUserViewCount());
                            vo.setCreateTime((entity.getCreatedTime()));
                            vo.setUpdateTime((entity.getUpdatedTime()));
                            voList.add(vo);
                        }
                    }
                }
                return voList;
            }
        }
        return Collections.emptyList();
    }

    private HelpDocVO convertEntity(HelpDocEntity entity) {
        HelpDocVO vo = new HelpDocVO();
        vo.setHelpDocId(entity.getId());
        vo.setTitle(entity.getTitle());
        vo.setHelpDocCatalogId(entity.getHelpDocCatalogId());
        vo.setAuthor(entity.getAuthor());
        vo.setSort(entity.getSort());
        vo.setPageViewCount(entity.getPageViewCount());
        vo.setUserViewCount(entity.getUserViewCount());
        vo.setCreateTime((entity.getCreatedTime()));
        vo.setUpdateTime((entity.getUpdatedTime()));
        return vo;
    }

    private HelpDocViewRecordVO convertRecord(HelpDocViewRecordEntity entity) {
        HelpDocViewRecordVO vo = new HelpDocViewRecordVO();
        vo.setUserId(entity.getUserId());
        vo.setPageViewCount(entity.getPageViewCount());
        vo.setCreateTime((entity.getCreatedTime()));
        vo.setUpdateTime((entity.getUpdatedTime()));
        return vo;
    }
}
