package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.gitee.dqcer.mcdull.framework.base.engine.CompareBean;
import io.gitee.dqcer.mcdull.framework.base.engine.DomainEngine;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.IpUtil;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.*;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.NoticeVisitbleRangeDataTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.*;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
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

    @Resource
    private IDepartmentService departmentService;

    @Resource
    private INoticeViewRecordService noticeViewRecordService;

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

        Integer createdBy = detail.getCreatedBy();
        UserEntity user = userService.get(Convert.toLong(createdBy));
        if (ObjUtil.isNotNull(user)) {
            vo.setCreateUserName(user.getActualName());
        }
        vo.setCreateTime(LocalDateTimeUtil.of(detail.getCreatedTime()));

        Boolean allVisibleFlag = vo.getAllVisibleFlag();
        if (BooleanUtil.isFalse(allVisibleFlag)) {
            List<NoticeVisibleRangeEntity> dbList =
                    noticeVisibleRangeService.getListByNoticeId(Convert.toLong(noticeId));

            Set<Integer> userIdSet = dbList.stream()
                    .filter(i -> i.getDataType().equals(NoticeVisitbleRangeDataTypeEnum.EMPLOYEE.getCode()))
                    .map(NoticeVisibleRangeEntity::getDataId)
                    .collect(Collectors.toSet());
            Map<Long, String> userMap = MapUtil.newHashMap();
            if (CollUtil.isNotEmpty(userIdSet)) {
                List<Long> userList = Convert.toList(Long.class, userIdSet);
                userMap = userService.getNameMap(new ArrayList<>(userList));
            }

            Set<Integer> deptIdSet = dbList.stream()
                    .filter(i -> i.getDataType().equals(NoticeVisitbleRangeDataTypeEnum.DEPARTMENT.getCode()))
                    .map(NoticeVisibleRangeEntity::getDataId)
                    .collect(Collectors.toSet());
            Map<Long, String> deptMap = MapUtil.newHashMap();
            if (CollUtil.isNotEmpty(deptIdSet)) {
                List<Long> deptList = Convert.toList(Long.class, deptIdSet);
                deptMap = departmentService.getNameMap(new ArrayList<>(deptList));
            }

            List<NoticeVisibleRangeVO> visibleRangeList = new ArrayList<>();
            for (NoticeVisibleRangeEntity item : dbList) {
                NoticeVisibleRangeVO rangeVO = new NoticeVisibleRangeVO();
                Integer dataId = item.getDataId();
                Integer dataType = item.getDataType();
                if (NoticeVisitbleRangeDataTypeEnum.EMPLOYEE.getCode().equals(dataType)) {
                    rangeVO.setDataName(userMap.get(Convert.toLong(dataId)));
                } else if (NoticeVisitbleRangeDataTypeEnum.DEPARTMENT.getCode().equals(dataType)) {
                    rangeVO.setDataName(deptMap.get(Convert.toLong(dataId)));
                }
                rangeVO.setDataType(dataType);
                rangeVO.setDataId(dataId);
                visibleRangeList.add(rangeVO);
            }
            vo.setVisibleRangeList(visibleRangeList);
        }
        return vo;
    }

    @Override
    public PagedVO<NoticeUserVO> queryUserNotice(NoticeEmployeeQueryDTO dto) {

        Long userId = UserContextHolder.userIdLong();

        List<Long> deptIdList = new ArrayList<>();
        UserEntity employeeEntity = userService.get(userId);
        Long departmentId = employeeEntity.getDepartmentId();
        if (departmentId != null) {
            deptIdList = departmentService.getChildrenIdList(departmentId);
            deptIdList.add(departmentId);
        }

        if (BooleanUtil.isTrue(dto.getNotViewFlag())) {
            Page<NoticeUserVO>  voPage = baseRepository.queryEmployeeNotViewNotice(dto, userId, deptIdList,
                    employeeEntity.getAdministratorFlag(),
                    NoticeVisitbleRangeDataTypeEnum.DEPARTMENT.getCode(),
                    NoticeVisitbleRangeDataTypeEnum.EMPLOYEE.getCode());
            List<NoticeUserVO> records = voPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                records.forEach(notice -> notice.setPublishDate(notice.getPublishTime().toLocalDate()));
            }
            return PageUtil.toPage(records, voPage);
        }
        return null;
    }

    @Override
    public NoticeDetailVO view(Long noticeId) {
        NoticeEntity entity = baseRepository.getById(noticeId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(noticeId);
        }
        // TODO: 2024/5/23 对不起，您没有权限查看内容

        Long userId = UserContextHolder.userIdLong();
        NoticeViewRecordEntity viewRecordEntity = noticeViewRecordService.getByUserIdAndNoticeId(userId, noticeId);
        String ipAddr = IpUtil.getIpAddr(ServletUtil.getRequest());
        if (ObjUtil.isNull(viewRecordEntity)) {
            NoticeViewRecordEntity newEntity = new NoticeViewRecordEntity();
            newEntity.setFirstIp(ipAddr);
            newEntity.setFirstUserAgent("");
            newEntity.setLastIp(ipAddr);
            newEntity.setLastUserAgent("");
            newEntity.setUserId(userId);
            newEntity.setNoticeId(noticeId);
            newEntity.setId(noticeId);
            newEntity.setPageViewCount(1);
            noticeViewRecordService.save(newEntity);
        } else {
            viewRecordEntity.setPageViewCount(viewRecordEntity.getPageViewCount() + 1);
            viewRecordEntity.setLastIp(ipAddr);
            // TODO: 2024/5/23
            viewRecordEntity.setLastUserAgent("");
            noticeViewRecordService.update(viewRecordEntity);
        }

        NoticeDetailVO vo = this.convertToDetailVo(entity);
        Integer noticeTypeId = vo.getNoticeTypeId();
        Map<Integer, String> noticeTypeMap = noticeTypeService.getMap(ListUtil.of(noticeTypeId));
        if (MapUtil.isNotEmpty(noticeTypeMap)) {
            vo.setNoticeTypeName(noticeTypeMap.get(noticeTypeId));
        }
        return vo;
    }

    private NoticeDetailVO convertToDetailVo(NoticeEntity entity) {
        NoticeDetailVO noticeDetailVO = new NoticeDetailVO();
        noticeDetailVO.setNoticeId(entity.getId());
        noticeDetailVO.setTitle(entity.getTitle());
        noticeDetailVO.setNoticeTypeId(entity.getNoticeTypeId());
        noticeDetailVO.setAllVisibleFlag(entity.getAllVisibleFlag());
        noticeDetailVO.setScheduledPublishFlag(entity.getScheduledPublishFlag());
        noticeDetailVO.setContentText(entity.getContentText());
        noticeDetailVO.setContentHtml(entity.getContentHtml());
        noticeDetailVO.setAttachment(entity.getAttachment());
        noticeDetailVO.setPublishTime(entity.getPublishTime());
        noticeDetailVO.setAuthor(entity.getAuthor());
        noticeDetailVO.setSource(entity.getSource());
        noticeDetailVO.setDocumentNumber(entity.getDocumentNumber());
        noticeDetailVO.setPageViewCount(entity.getPageViewCount());
        noticeDetailVO.setUserViewCount(entity.getUserViewCount());
        noticeDetailVO.setCreateTime(LocalDateTimeUtil.of(entity.getCreatedTime()));
        noticeDetailVO.setUpdateTime(LocalDateTimeUtil.of(entity.getUpdatedTime()));
        return noticeDetailVO;
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