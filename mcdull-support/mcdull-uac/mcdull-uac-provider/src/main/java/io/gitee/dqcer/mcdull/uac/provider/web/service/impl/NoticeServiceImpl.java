package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeUpdateFormVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


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

    @Override
    public PagedVO<NoticeVO> queryPage(NoticeQueryDTO dto) {
        List<NoticeVO> voList = new ArrayList<>();
        Page<NoticeEntity> entityPage = baseRepository.selectPage(dto);
        List<NoticeEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            for (NoticeEntity entity : recordList) {
                NoticeVO vo = this.convertToVO(entity);
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
        baseRepository.save(entity);
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
        baseRepository.updateById(entity);
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
