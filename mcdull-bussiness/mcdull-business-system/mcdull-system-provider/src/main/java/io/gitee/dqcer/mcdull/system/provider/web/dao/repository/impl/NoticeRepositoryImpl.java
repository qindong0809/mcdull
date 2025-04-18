package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeEmployeeQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.NoticeQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.NoticeUserVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.NoticeMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.INoticeRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Notice RepositoryImpl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class NoticeRepositoryImpl extends
        CrudRepository<NoticeMapper, NoticeEntity> implements INoticeRepository {

    @Override
    public List<NoticeEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<NoticeEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(NoticeEntity::getId, idList);
        List<NoticeEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        wrapper.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return Collections.emptyList();
    }


    @Override
    public Page<NoticeEntity> selectPage(NoticeQueryDTO param) {
        LambdaQueryWrapper<NoticeEntity> lambda = Wrappers.lambdaQuery();
        Integer noticeTypeId = param.getNoticeTypeId();
        if (ObjUtil.isNotNull(noticeTypeId)) {
            lambda.eq(NoticeEntity::getNoticeTypeId, noticeTypeId);
        }
        String documentNumber = param.getDocumentNumber();
        if (ObjUtil.isNotEmpty(documentNumber)) {
            lambda.like(NoticeEntity::getDocumentNumber, documentNumber);
        }
        String keywords = param.getKeywords();
        if (StrUtil.isNotBlank(keywords)) {
            lambda.and(i -> i.like(NoticeEntity::getAuthor, keywords)
                    .or().like(NoticeEntity::getSource, keywords)
                    .or().like(NoticeEntity::getTitle, keywords));
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    @Override
    public Page<NoticeUserVO> queryEmployeeNotViewNotice(NoticeEmployeeQueryDTO dto,
                                                         Integer userId,
                                                         List<Integer> deptIdList,
                                                         Boolean administratorFlag,
                                                         Integer deptCode,
                                                         Integer userCode) {
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper
                .queryEmployeeNotViewNotice(page, dto, userId, deptIdList, administratorFlag, deptCode, userCode);
    }

    @Override
    public Page<NoticeUserVO> queryEmployeeNotice(NoticeEmployeeQueryDTO dto,
                                                  Integer userId,
                                                  List<Integer> deptIdList,
                                                  Boolean administratorFlag,
                                                  Integer deptCode,
                                                  Integer userCode) {
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper
                .queryEmployeeNotice(page, dto, userId, deptIdList, administratorFlag, deptCode, userCode);
    }

    @Override
    public List<NoticeEntity> listByTitleName(String title) {
        if (StrUtil.isNotBlank(title)) {
            LambdaQueryWrapper<NoticeEntity> query = Wrappers.lambdaQuery();
            query.eq(NoticeEntity::getTitle, title);
            return baseMapper.selectList(query);
        }
        return Collections.emptyList();
    }
}