package io.gitee.dqcer.blaze.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.blaze.dao.mapper.BlazeOrderDetailMapper;
import io.gitee.dqcer.blaze.dao.repository.IBlazeOrderDetailRepository;
import io.gitee.dqcer.blaze.domain.entity.BlazeOrderDetailEntity;
import io.gitee.dqcer.blaze.domain.form.BlazeOrderDetailQueryDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;


/**
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */
@Service
public class BlazeOrderDetailRepositoryImpl extends
        ServiceImpl<BlazeOrderDetailMapper, BlazeOrderDetailEntity> implements IBlazeOrderDetailRepository {

    @Override
    public Page<BlazeOrderDetailEntity> selectPage(BlazeOrderDetailQueryDTO dto) {
        LambdaQueryWrapper<BlazeOrderDetailEntity> query = Wrappers.lambdaQuery();
        Boolean isTalent = dto.getIsTalent();
        if (ObjUtil.isNotNull(isTalent)) {
            query.eq(BlazeOrderDetailEntity::getIsTalent, isTalent);
        }
        Integer responsibleUserId = dto.getResponsibleUserId();
        if (ObjUtil.isNotNull(responsibleUserId)) {
            query.eq(BlazeOrderDetailEntity::getResponsibleUserId, responsibleUserId);
        }
        query.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }
}