package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
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
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeViewRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeVisibleRangeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.NoticeVisitbleRangeDataTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.*;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
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
            LocalDateTime now = LocalDateTime.now();

            Map<Integer, String> nameMap = userService.getNameMap(new ArrayList<>(userIdSet));
            for (NoticeEntity entity : recordList) {
                NoticeVO vo = this.convertToVO(entity);
                vo.setCreateUserName(nameMap.get(entity.getCreatedBy()));
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
        UserEntity user = userService.get(createdBy);
        if (ObjUtil.isNotNull(user)) {
            vo.setCreateUserName(user.getActualName());
        }
        vo.setCreateTime(LocalDateTimeUtil.of(detail.getCreatedTime()));

        Boolean allVisibleFlag = vo.getAllVisibleFlag();
        if (BooleanUtil.isFalse(allVisibleFlag)) {
            List<NoticeVisibleRangeEntity> dbList =
                    noticeVisibleRangeService.getListByNoticeId(noticeId);

            Set<Integer> userIdSet = dbList.stream()
                    .filter(i -> i.getDataType().equals(NoticeVisitbleRangeDataTypeEnum.EMPLOYEE.getCode()))
                    .map(NoticeVisibleRangeEntity::getDataId)
                    .collect(Collectors.toSet());
            Map<Integer, String> userMap = MapUtil.newHashMap();
            if (CollUtil.isNotEmpty(userIdSet)) {
                userMap = userService.getNameMap(new ArrayList<>(userIdSet));
            }

            Set<Integer> deptIdSet = dbList.stream()
                    .filter(i -> i.getDataType().equals(NoticeVisitbleRangeDataTypeEnum.DEPARTMENT.getCode()))
                    .map(NoticeVisibleRangeEntity::getDataId)
                    .collect(Collectors.toSet());
            Map<Integer, String> deptMap = MapUtil.newHashMap();
            if (CollUtil.isNotEmpty(deptIdSet)) {
                deptMap = departmentService.getNameMap(new ArrayList<>(deptIdSet));
            }

            List<NoticeVisibleRangeVO> visibleRangeList = new ArrayList<>();
            for (NoticeVisibleRangeEntity item : dbList) {
                NoticeVisibleRangeVO rangeVO = new NoticeVisibleRangeVO();
                Integer dataId = item.getDataId();
                Integer dataType = item.getDataType();
                if (NoticeVisitbleRangeDataTypeEnum.EMPLOYEE.getCode().equals(dataType)) {
                    rangeVO.setDataName(userMap.get(dataId));
                } else if (NoticeVisitbleRangeDataTypeEnum.DEPARTMENT.getCode().equals(dataType)) {
                    rangeVO.setDataName(deptMap.get(dataId));
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

        Integer userId = UserContextHolder.userId();

        List<Integer> deptIdList = new ArrayList<>();
        UserEntity userEntity = userService.get(userId);
        Integer departmentId = userEntity.getDepartmentId();
        if (departmentId != null) {
            deptIdList = departmentService.getChildrenIdList(departmentId);
            deptIdList.add(departmentId);
        }
        if (BooleanUtil.isTrue(dto.getNotViewFlag())) {
            Page<NoticeUserVO> voPage = baseRepository.queryEmployeeNotViewNotice(dto, userId, deptIdList,
                    userEntity.getAdministratorFlag(),
                    NoticeVisitbleRangeDataTypeEnum.DEPARTMENT.getCode(),
                    NoticeVisitbleRangeDataTypeEnum.EMPLOYEE.getCode());
            List<NoticeUserVO> records = voPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                records.forEach(notice -> notice.setPublishDate(notice.getPublishTime().toLocalDate()));
            }
            return PageUtil.toPage(records, voPage);
        }
        List<NoticeEntity> noticeEntityList = this.getNoticeEntities(userId, userEntity.getAdministratorFlag());
        List<NoticeUserVO> voAllList = new ArrayList<>();
        noticeEntityList = this.filter(dto, noticeEntityList);
        if (CollUtil.isNotEmpty(noticeEntityList)) {
            Set<Integer> typeIdSet = noticeEntityList.stream().map(NoticeEntity::getNoticeTypeId).collect(Collectors.toSet());
            Map<Integer, String> noticeTypeMap = noticeTypeService.getMap(new ArrayList<>(typeIdSet));
            for (NoticeEntity notice : noticeEntityList) {
                NoticeUserVO noticeUserVO = this.convertToNoticeUserVO(notice);
                if (BooleanUtil.isTrue(notice.getScheduledPublishFlag())) {
                    noticeUserVO.setPublishDate(notice.getPublishTime().toLocalDate());
                }
                noticeUserVO.setNoticeTypeName(noticeTypeMap.get(notice.getNoticeTypeId()));
                NoticeViewRecordEntity viewRecord = noticeViewRecordService.getByUserIdAndNoticeId(userId, notice.getId());
                Integer pv = GlobalConstant.Number.NUMBER_0;
                if (ObjUtil.isNotNull(viewRecord)) {
                    pv = viewRecord.getPageViewCount();
                }
                noticeUserVO.setViewFlag(pv > GlobalConstant.Number.NUMBER_0);
                voAllList.add(noticeUserVO);
            }
        }
        return  PageUtil.of(voAllList, Convert.toInt(dto.getPageSize()), Convert.toInt(dto.getPageNum()));
    }

    private List<NoticeEntity> filter(NoticeEmployeeQueryDTO dto, List<NoticeEntity> noticeEntityList) {
        Integer noticeTypeId = dto.getNoticeTypeId();
        if (ObjUtil.isNotNull(noticeTypeId)) {
            return noticeEntityList.stream().filter(i->i.getNoticeTypeId().equals(Convert.toInt(noticeTypeId))).collect(Collectors.toList());
        }
        return noticeEntityList;
    }

    private NoticeUserVO convertToNoticeUserVO(NoticeEntity entity) {
        NoticeUserVO noticeUserVO = new NoticeUserVO();
        noticeUserVO.setPublishDate(LocalDate.from(LocalDateTimeUtil.of(entity.getPublishTime())));
        noticeUserVO.setNoticeId(entity.getId());
        noticeUserVO.setTitle(entity.getTitle());
        noticeUserVO.setNoticeTypeId(entity.getNoticeTypeId());
        noticeUserVO.setAllVisibleFlag(entity.getAllVisibleFlag());
        noticeUserVO.setScheduledPublishFlag(entity.getScheduledPublishFlag());
        noticeUserVO.setPublishFlag(entity.getScheduledPublishFlag());
        noticeUserVO.setPublishTime(entity.getPublishTime());
        noticeUserVO.setAuthor(entity.getAuthor());
        noticeUserVO.setSource(entity.getSource());
        noticeUserVO.setDocumentNumber(entity.getDocumentNumber());
        noticeUserVO.setPageViewCount(entity.getPageViewCount());
        noticeUserVO.setUserViewCount(entity.getUserViewCount());
        noticeUserVO.setDeletedFlag(entity.getDelFlag());
        noticeUserVO.setCreateTime(LocalDateTimeUtil.of(entity.getCreatedTime()));
        noticeUserVO.setUpdateTime(LocalDateTimeUtil.of(entity.getUpdatedTime()));
        return noticeUserVO;
    }

    private List<NoticeEntity> getNoticeEntities(Integer userId, Boolean administratorFlag) {
        if (BooleanUtil.isFalse(administratorFlag)) {
            List<Integer> noticeIdList = noticeViewRecordService.getByUserId(userId);
            if (CollUtil.isEmpty(noticeIdList)) {
                return Collections.emptyList();
            }
            return baseRepository.queryListByIds(Convert.toList(Integer.class, noticeIdList));
        }
        return baseRepository.list();
    }

    @Override
    public NoticeDetailVO view(Integer noticeId) {
        NoticeEntity entity = baseRepository.getById(noticeId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(noticeId);
        }
        // TODO: 2024/5/23 对不起，您没有权限查看内容

        Integer userId = UserContextHolder.userId();
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
        Integer id = dto.getNoticeId();
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
