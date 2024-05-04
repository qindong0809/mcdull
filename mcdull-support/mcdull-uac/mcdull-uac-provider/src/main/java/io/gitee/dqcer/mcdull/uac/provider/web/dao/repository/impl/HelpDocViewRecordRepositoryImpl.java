package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocViewRecordQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocViewRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.HelpDocViewRecordMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocViewRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class HelpDocViewRecordRepositoryImpl
        extends ServiceImpl<HelpDocViewRecordMapper, HelpDocViewRecordEntity>  implements IHelpDocViewRecordRepository {

    private static final Logger log = LoggerFactory.getLogger(HelpDocViewRecordRepositoryImpl.class);

    @Override
    public Page<HelpDocViewRecordEntity> selectPage(HelpDocViewRecordQueryDTO dto) {
        LambdaQueryWrapper<HelpDocViewRecordEntity> lambda = new QueryWrapper<HelpDocViewRecordEntity>().lambda();
        Long helpDocId = dto.getHelpDocId();
        if (ObjUtil.isNotNull(helpDocId)) {
            lambda.eq(HelpDocViewRecordEntity::getHelpDocId, helpDocId);
        }
        Long userId = dto.getUserId();
        if (ObjUtil.isNotNull(userId)) {
            lambda.eq(HelpDocViewRecordEntity::getUserId, userId);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }
}