package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.engine.CompareBean;
import io.gitee.dqcer.mcdull.framework.base.engine.DomainEngine;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeVisibleRangeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDataScopeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeUpdateFormVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeTypeService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeVisibleRangeService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class NoticeServiceImpl
        extends BasicServiceImpl<INoticeRepository> implements INoticeService {

    @Resource
    private IUserService userService;

    @Resource
    private INoticeTypeService noticeTypeService;

    @Resource
    private INoticeVisibleRangeService noticeVisibleRangeService;

    @Override
    public PagedVO<NoticeVO> queryPage(NoticeQueryDTO dto) {
        List<NoticeVO> voList = new ArrayList<>();
        Page<NoticeEntity> entityPage = baseRepository.selectPage(dto);
        List<NoticeEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            Set<Integer> typeIdSet = recordList.stream()
                    .map(NoticeEntity::getNoticeTypeId).collect(Collectors.toSet());
            Map<Integer, String> noticeTypeServiceMap =
                    noticeTypeService.getMap(new ArrayList<>(typeIdSet));
            Set<Integer> userIdSet = recordList.stream()
                    .map(BaseEntity::getCreatedBy).collect(Collectors.toSet());
            List<Long> userList = new ArrayList<>();
            for (Integer userId : userIdSet) {
                userList.add(Convert.toLong(userId));
            }
            LocalDateTime now = LocalDateTime.now();

            Map<Long, String> nameMap = userService.getNameMap(new ArrayList<>(userList));
            for (NoticeEntity entity : recordList) {
                NoticeVO vo = this.convertToVO(entity);
                vo.setCreateUserName(nameMap.get(Convert.toLong(entity.getCreatedBy())));
                vo.setNoticeTypeName(noticeTypeServiceMap.get(entity.getNoticeTypeId()));
                vo.setPublishFlag(vo.getPublishTime().isBefore(now));

                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public NoticeUpdateFormVO getUpdateFormVO(Integer noticeId) {
        NoticeEntity detail = baseRepository.getById(noticeId);
        if (ObjUtil.isNull(detail)) {
            this.throwDataNotExistException(noticeId);
        }
        NoticeUpdateFormVO vo = new NoticeUpdateFormVO();
        vo.setNoticeId(detail.getId());
        vo.setNoticeTypeId(detail.getNoticeTypeId());
        vo.setTitle(detail.getTitle());
        vo.setAllVisibleFlag(detail.getAllVisibleFlag());
        vo.setScheduledPublishFlag(detail.getScheduledPublishFlag());
        vo.setPublishTime(detail.getPublishTime());
        vo.setPublishFlag(detail.getScheduledPublishFlag());
        vo.setContentHtml(detail.getContentHtml());
        vo.setSource(detail.getSource());
        vo.setDocumentNumber(detail.getDocumentNumber());
        vo.setPageViewCount(detail.getPageViewCount());
        vo.setUserViewCount(detail.getUserViewCount());
        // TODO: 2024/5/21
//            vo.setAttachment(detail.getAttachment());
        vo.setAuthor(detail.getAuthor());
        // TODO: 2024/5/21
        vo.setVisibleRangeList(new ArrayList<>());

        Integer createdBy = detail.getCreatedBy();
        UserEntity user = userService.get(Convert.toLong(createdBy));
        if (ObjUtil.isNotNull(user)) {
            vo.setCreateUserName(user.getActualName());
        }
        vo.setCreateTime(LocalDateTimeUtil.of(detail.getCreatedTime()));
        return vo;
    }

    private NoticeVO convertToVO(NoticeEntity item){
        NoticeVO vo = new NoticeVO();
        vo.setNoticeId(item.getId());
        vo.setNoticeTypeId(item.getNoticeTypeId());
        vo.setTitle(item.getTitle());
        vo.setAllVisibleFlag(item.getAllVisibleFlag());
        vo.setScheduledPublishFlag(item.getScheduledPublishFlag());
        vo.setPublishTime(item.getPublishTime());
        vo.setPageViewCount(item.getPageViewCount());
        vo.setUserViewCount(item.getUserViewCount());
        vo.setSource(item.getSource());
        vo.setAuthor(item.getAuthor());
        vo.setDocumentNumber(item.getDocumentNumber());
        vo.setCreateTime(LocalDateTimeUtil.of(item.getCreatedTime()));
        vo.setDeletedFlag(item.getDelFlag());
        return vo;
    }

    private void setUpdateFieldValue(NoticeUpdateDTO item, NoticeEntity entity){
        entity.setNoticeTypeId(item.getNoticeTypeId());
        entity.setTitle(item.getTitle());
        entity.setAllVisibleFlag(item.getAllVisibleFlag());
        entity.setScheduledPublishFlag(item.getScheduledPublishFlag());
        entity.setPublishTime(item.getPublishTime());
        entity.setContentText(item.getContentText());
        entity.setContentHtml(item.getContentHtml());
        entity.setAttachment(item.getAttachment());
        entity.setSource(item.getSource());
        entity.setAuthor(item.getAuthor());
        entity.setDocumentNumber(item.getDocumentNumber());
    }

    private NoticeEntity convertToEntity(NoticeAddDTO item){
        NoticeEntity entity = new NoticeEntity();
        entity.setNoticeTypeId(item.getNoticeTypeId());
        entity.setTitle(item.getTitle());
        entity.setAllVisibleFlag(item.getAllVisibleFlag());
        entity.setScheduledPublishFlag(item.getScheduledPublishFlag());
        entity.setPublishTime(item.getPublishTime());
        entity.setContentText(item.getContentText());
        entity.setContentHtml(item.getContentHtml());
        entity.setAttachment(item.getAttachment());
        entity.setPageViewCount(0);
        entity.setUserViewCount(0);
        entity.setInactive(false);
        entity.setSource(item.getSource());
        entity.setAuthor(item.getAuthor());
        entity.setDocumentNumber(item.getDocumentNumber());
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(NoticeAddDTO dto) {
        NoticeEntity entity = this.convertToEntity(dto);
        if (BooleanUtil.isFalse(dto.getScheduledPublishFlag())) {
            entity.setPublishTime(LocalDateTime.now());
        }
        baseRepository.save(entity);

        Integer id = entity.getId();

        if (BooleanUtil.isFalse(dto.getAllVisibleFlag())) {
            List<NoticeVisibleRangeDTO> visibleRangeList = dto.getVisibleRangeList();
            if (CollUtil.isNotEmpty(visibleRangeList)) {
                List<NoticeVisibleRangeEntity> rangeEntityList = new ArrayList<>();
                for (NoticeVisibleRangeDTO rangeDTO : visibleRangeList) {
                    NoticeVisibleRangeEntity rangeEntity = new NoticeVisibleRangeEntity();
                    rangeEntity.setNoticeId(id);
                    rangeEntity.setDataId(rangeDTO.getDataId());
                    rangeEntity.setDataType(rangeDTO.getDataType());
                    rangeEntityList.add(rangeEntity);
                }
                noticeVisibleRangeService.batchInsert(rangeEntityList);
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(NoticeUpdateDTO dto) {
        Long id = dto.getNoticeId();
        NoticeEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.setUpdateFieldValue(dto, entity);
        if (BooleanUtil.isFalse(dto.getScheduledPublishFlag())) {
            entity.setPublishTime(LocalDateTime.now());
        }
        baseRepository.updateById(entity);

        List<NoticeVisibleRangeDTO> visibleRangeList = dto.getVisibleRangeList();

        List<NoticeVisibleRangeEntity> dbList = noticeVisibleRangeService.getListByNoticeId(id);
        List<NoticeVisibleRangeEntity> tempList = new ArrayList<>();
        if (CollUtil.isNotEmpty(visibleRangeList)) {
            Integer anInt = Convert.toInt(id);
            for (NoticeVisibleRangeDTO item : visibleRangeList) {
                NoticeVisibleRangeEntity temp = new NoticeVisibleRangeEntity();
                temp.setNoticeId(anInt);
                temp.setDataId(item.getDataId());
                temp.setDataType(item.getDataType());
                if (CollUtil.isNotEmpty(dbList)) {
                    NoticeVisibleRangeEntity rangeEntity = dbList.stream().filter(
                            i -> i.getNoticeId().equals(anInt)
                                    && i.getDataId().equals(item.getDataId())
                                    && i.getDataType().equals(item.getDataType())).findFirst().orElse(null);
                    if (ObjUtil.isNotNull(rangeEntity)) {
                        temp.setId(rangeEntity.getId());
                    }
                }
                tempList.add(temp);
            }
        }
        CompareBean<NoticeVisibleRangeEntity, Integer> compare = DomainEngine.compare(dbList, tempList);
        noticeVisibleRangeService.update(compare.getInsertList(), compare.getUpdateList(), compare.getRemoveList());
    }

    @Override
    public NoticeVO detail(Integer id) {
        NoticeEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        return this.convertToVO(entity);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Integer> idList) {
        List<NoticeEntity> entityList = baseRepository.queryListByIds(idList);
        if (entityList.size() != idList.size()) {
            this.throwDataNotExistException(idList);
        }
        baseRepository.removeBatchByIds(idList);
    }

}
