package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.database.BackListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.BackEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.BackMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IBackRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
* back 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class BackRepositoryImpl extends ServiceImpl<BackMapper, BackEntity>  implements IBackRepository {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<BackEntity> listByBizId(Long ticketId, Integer modelTicket) {
        LambdaQueryWrapper<BackEntity> query = Wrappers.lambdaQuery();
        query.eq(BackEntity::getBizId, ticketId);
        query.eq(BackEntity::getModel, modelTicket);
        List<BackEntity> list = baseMapper.selectList(query);
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BackEntity> selectPage(BackListDTO dto) {
        LambdaQueryWrapper<BackEntity> lambda = new QueryWrapper<BackEntity>().lambda();
        lambda.eq(BackEntity::getBizId, dto.getTicketId());
        lambda.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateToDelete(Long id) {
        LambdaUpdateWrapper<BackEntity> update = Wrappers.lambdaUpdate();
        update.eq(IdEntity::getId, id);
        baseMapper.update(null, update);
    }
}