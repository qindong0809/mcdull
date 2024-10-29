package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocViewRecordQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocViewRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.HelpDocViewRecordMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocViewRecordRepository;
import org.springframework.stereotype.Service;

/**
 * Help Doc View Record Repository Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class HelpDocViewRecordRepositoryImpl
        extends CrudRepository<HelpDocViewRecordMapper, HelpDocViewRecordEntity> implements IHelpDocViewRecordRepository {

    @Override
    public Page<HelpDocViewRecordEntity> selectPage(HelpDocViewRecordQueryDTO dto) {
        LambdaQueryWrapper<HelpDocViewRecordEntity> lambda = new QueryWrapper<HelpDocViewRecordEntity>().lambda();
        Integer helpDocId = dto.getHelpDocId();
        if (ObjUtil.isNotNull(helpDocId)) {
            lambda.eq(HelpDocViewRecordEntity::getHelpDocId, helpDocId);
        }
        Integer userId = dto.getUserId();
        if (ObjUtil.isNotNull(userId)) {
            lambda.eq(HelpDocViewRecordEntity::getUserId, userId);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }
}