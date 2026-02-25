package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeTypeEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeTypeVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.NoticeTypeMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.INoticeTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Notice Type Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class NoticeTypeServiceImpl
        extends BasicCurdServiceImpl<NoticeTypeMapper, NoticeTypeEntity> implements INoticeTypeService {


    @Override
    public List<NoticeTypeVO> getAll() {
        List<NoticeTypeVO> voList = new ArrayList<>();
        List<NoticeTypeEntity> list = super.list();
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
        List<NoticeTypeEntity> list = super.list();
        if (CollUtil.isNotEmpty(list)) {
            LogicCheckUtil.validNameExist(null, name, list,
                    (entity) -> entity.getNoticeTypeName().equals(name));
        }
        NoticeTypeEntity entity = new NoticeTypeEntity();
        entity.setNoticeTypeName(name);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Integer id, String name) {
        NoticeTypeEntity typeEntity = super.mustGet(id);
        List<NoticeTypeEntity> list = super.list();
        if (CollUtil.isNotEmpty(list)) {
            LogicCheckUtil.validNameExist(id, name, list,
                    (entity) -> !id.equals(entity.getId())
                            && entity.getNoticeTypeName().equals(name));
        }
        typeEntity.setNoticeTypeName(name);
        super.save(typeEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        NoticeTypeEntity typeEntity = super.mustGet(id);
        super.removeById(id);
    }

    @Override
    public Map<Integer, String> getMap(List<Integer> idList) {
        List<NoticeTypeEntity> list = super.listByIds(idList);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().collect(
                    Collectors.toMap(NoticeTypeEntity::getId, NoticeTypeEntity::getNoticeTypeName));
        }
        return Collections.emptyMap();
    }
}
