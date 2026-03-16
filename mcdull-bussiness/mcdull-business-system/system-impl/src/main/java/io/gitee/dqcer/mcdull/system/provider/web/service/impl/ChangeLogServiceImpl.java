package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
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
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.framework.web.version.IVersionInfoComponent;
import io.gitee.dqcer.mcdull.system.provider.model.audit.ChangeLogAudit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.ChangeLogEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.ChangeLogTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ChangeLogAndVersionVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ChangeLogVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.ChangeLogMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IChangeLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Change Log Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class ChangeLogServiceImpl
        extends BasicCurdServiceImpl<ChangeLogMapper, ChangeLogEntity> implements IChangeLogService {

    @Resource
    private IVersionInfoComponent versionInfoComponent;
    @Resource
    private IAuditManager auditManager;
    @Resource
    private ICommonManager commonManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(ChangeLogAddDTO dto) {
        List<ChangeLogEntity> list = super.list();
        if (CollUtil.isNotEmpty(list)) {
            LogicCheckUtil.validNameExist(null, dto.getVersion(), list,
                    entity -> entity.getVersion().equals(dto.getVersion()));
        }
        ChangeLogEntity entity = this.convertEntity(dto);
        baseMapper.insert(entity);
        auditManager.saveByAddEnum(dto.getVersion(), entity.getId(), this.buildAuditLog(entity));
        return true;
    }

    private Audit buildAuditLog(ChangeLogEntity entity) {
        ChangeLogAudit audit = new ChangeLogAudit();
        audit.setVersion(entity.getVersion());
        audit.setContent(entity.getContent());
        audit.setPublicDate(entity.getPublicDate().toString());
        audit.setPublishAuthor(entity.getPublishAuthor());
        audit.setLink(entity.getLink());
        return audit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ChangeLogUpdateDTO dto) {
        Integer changeLogId = dto.getChangeLogId();
        ChangeLogEntity logEntity = super.getById(changeLogId);
        if (ObjUtil.isNull(logEntity)) {
            LogicCheckUtil.throwDataNotExistException(changeLogId);
        }
        ChangeLogEntity oldEntity = ObjUtil.cloneByStream(logEntity);
        List<ChangeLogEntity> list = super.list();
        if (CollUtil.isNotEmpty(list)) {
            LogicCheckUtil.validNameExist(changeLogId, dto.getVersion(), list,
                    entity -> (!changeLogId.equals(entity.getId())) && entity.getVersion().equals(dto.getVersion()));
        }
        this.settingUpdateField(dto, logEntity);
        baseMapper.updateById(logEntity);
        auditManager.saveByUpdateEnum(dto.getVersion(), changeLogId,
                this.buildAuditLog(oldEntity), this.buildAuditLog(logEntity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<Integer> idList) {
        List<ChangeLogEntity> entityList = this.queryListByIds(idList);
        if (entityList.size() != idList.size()) {
            LogicCheckUtil.throwDataNotExistException(idList);
        }
        super.removeByIds(idList);
        for (ChangeLogEntity entity : entityList) {
            auditManager.saveByDeleteEnum(entity.getVersion(), entity.getId(), null);
        }
    }

    @Override
    public PagedVO<ChangeLogVO> queryPage(ChangeLogQueryDTO dto) {
        Page<ChangeLogEntity> entityPage = this.selectPage(dto);
        List<ChangeLogVO> voList = new ArrayList<>();
        for (ChangeLogEntity entity : entityPage.getRecords()) {
            ChangeLogVO logVO = this.convertToConfigVO(entity);
            logVO.setTypeName(IEnum.getTextByCode(ChangeLogTypeEnum.class, entity.getType()));
            voList.add(logVO);
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public ChangeLogVO getById(Integer id) {
        ChangeLogEntity logEntity = super.getById(id);
        if (ObjUtil.isNull(logEntity)) {
            LogicCheckUtil.throwDataNotExistException(id);
        }
        return this.convertToConfigVO(logEntity);
    }

    @Override
    public ChangeLogAndVersionVO getChangeLogAndVersion() {
        ChangeLogAndVersionVO changeLogAndVersion = new ChangeLogAndVersionVO();
        List<ChangeLogEntity> list = super.list();
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

    @Override
    public boolean exportData(ChangeLogQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitlePairList());
        return true;
    }

    private List<Pair<String, Func1<ChangeLogVO, ?>>> getTitlePairList() {
        List<Pair<String, Func1<ChangeLogVO, ?>>>  titleList = new ArrayList<>();
        titleList.add(Pair.of("版本", ChangeLogVO::getVersion));
        titleList.add(Pair.of("更新类型", ChangeLogVO::getTypeName));
        titleList.add(Pair.of("发布人", ChangeLogVO::getPublishAuthor));
        titleList.add(Pair.of("发布日期", ChangeLogVO::getPublicDate));
        titleList.add(Pair.of("更新内容", ChangeLogVO::getContent));
        titleList.add(Pair.of("跳转链接", ChangeLogVO::getLink));
        titleList.add(Pair.of("创建时间", ChangeLogVO::getCreateTime));
        titleList.add(Pair.of("更新时间", ChangeLogVO::getUpdateTime));
        return titleList;
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


    public List<ChangeLogEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<ChangeLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ChangeLogEntity::getId, idList);
        List<ChangeLogEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }


    public Page<ChangeLogEntity> selectPage(ChangeLogQueryDTO param) {
        LambdaQueryWrapper<ChangeLogEntity> lambda = Wrappers.lambdaQuery();
        String keyword = param.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.and(i->i.like(ChangeLogEntity::getVersion, keyword)
                .or().like(ChangeLogEntity::getContent, keyword));
        }
        Integer type = param.getType();
        if (ObjUtil.isNotNull(type)) {
            lambda.eq(ChangeLogEntity::getType, type);
        }
        LocalDate startDate = param.getPublicDateBegin();
        LocalDate endDate = param.getPublicDateEnd();
        if (ObjUtil.isAllNotEmpty(startDate, endDate)) {
            lambda.between(ChangeLogEntity::getPublicDate, startDate,
                LocalDateTimeUtil.endOfDay(endDate.atStartOfDay()));
        }
        LocalDate createTime = param.getCreateTime();
        if (ObjUtil.isNotNull(createTime)) {
            lambda.between(RelEntity::getCreatedTime, createTime, LocalDateTimeUtil.endOfDay(createTime.atStartOfDay()));
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }


    public void insert(ChangeLogEntity entity) {
        baseMapper.insert(entity);
    }

    public boolean exist(ChangeLogEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}
