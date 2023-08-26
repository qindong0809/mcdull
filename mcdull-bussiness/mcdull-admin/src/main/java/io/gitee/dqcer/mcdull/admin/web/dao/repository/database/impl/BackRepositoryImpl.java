package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.database.BackListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.BackDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.BackMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IBackRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.entity.MiddleDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
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
public class BackRepositoryImpl extends ServiceImpl<BackMapper, BackDO>  implements IBackRepository {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<BackDO> listByBizId(Long ticketId, Integer modelTicket) {
        LambdaQueryWrapper<BackDO> query = Wrappers.lambdaQuery();
        query.eq(BackDO::getBizId, ticketId);
        query.eq(BackDO::getModel, modelTicket);
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        List<BackDO> list = baseMapper.selectList(query);
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BackDO> selectPage(BackListDTO dto) {
        LambdaQueryWrapper<BackDO> lambda = new QueryWrapper<BackDO>().lambda();
        lambda.eq(BaseDO::getDelBy, DelFlayEnum.NORMAL.getCode());
        lambda.orderByDesc(MiddleDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateToDelete(Long id) {
        LambdaUpdateWrapper<BackDO> update = Wrappers.lambdaUpdate();
        update.set(BaseDO::getDelFlag, DelFlayEnum.DELETED.getCode());
        update.eq(IdDO::getId, id);
        baseMapper.update(null, update);
    }
}