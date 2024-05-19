package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.NoticeQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.NoticeEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.NoticeMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.INoticeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* 系统配置 数据库操作封装实现层
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class NoticeRepositoryImpl extends
        ServiceImpl<NoticeMapper, NoticeEntity> implements INoticeRepository {

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
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            // TODO 组装查询条件
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }
}