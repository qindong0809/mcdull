package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        entity.setPageViewCount(item.getPageViewCount());
        entity.setUserViewCount(item.getUserViewCount());
        entity.setSource(item.getSource());
        entity.setAuthor(item.getAuthor());
        entity.setDocumentNumber(item.getDocumentNumber());
        entity.setInactive(item.getInactive());
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
        entity.setPageViewCount(item.getPageViewCount());
        entity.setUserViewCount(item.getUserViewCount());
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
