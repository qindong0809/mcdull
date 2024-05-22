package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ChangeLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeTypeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeTypeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IChangeLogRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeTypeRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class NoticeTypeServiceImpl
        extends BasicServiceImpl<INoticeTypeRepository> implements INoticeTypeService {


    @Override
    public List<NoticeTypeVO> getAll() {
        List<NoticeTypeVO> voList = new ArrayList<>();
        List<NoticeTypeEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            for (NoticeTypeEntity entity : list) {
                voList.add(this.convertVO(entity));
            }
        }
        return voList;
    }

    private NoticeTypeVO convertVO(NoticeTypeEntity entity) {
        NoticeTypeVO noticeTypeVO = new NoticeTypeVO();
        noticeTypeVO.setNoticeTypeId(entity.getId());
        noticeTypeVO.setNoticeTypeName(entity.getNoticeTypeName());
        return noticeTypeVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(String name) {
        List<NoticeTypeEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(null, name, list,
                    (entity) -> entity.getNoticeTypeName().equals(name));
        }
        NoticeTypeEntity entity = new NoticeTypeEntity();
        entity.setNoticeTypeName(name);
        baseRepository.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Integer id, String name) {
        NoticeTypeEntity typeEntity = baseRepository.getById(id);
        if (ObjUtil.isNull(typeEntity)) {
            this.throwDataNotExistException(id);
        }
        List<NoticeTypeEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(id, name, list,
                    (entity) -> !id.equals(entity.getId()) && entity.getNoticeTypeName().equals(name));
        }
        typeEntity.setNoticeTypeName(name);
        baseRepository.save(typeEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        NoticeTypeEntity typeEntity = baseRepository.getById(id);
        if (ObjUtil.isNull(typeEntity)) {
            this.throwDataNotExistException(id);
        }
        baseRepository.removeById(id);
    }

    @Override
    public Map<Integer, String> getMap(List<Integer> idList) {
        List<NoticeTypeEntity> list = baseRepository.listByIds(idList);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().collect(
                    Collectors.toMap(NoticeTypeEntity::getId, NoticeTypeEntity::getNoticeTypeName));
        }
        return Collections.emptyMap();
    }
}
