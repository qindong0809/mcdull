package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.HelpDocViewRecordQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.HelpDocViewRecordEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.HelpDocViewRecordMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IHelpDocViewRecordService;
import org.springframework.stereotype.Service;

@Service
public class HelpDocViewRecordImpl extends BasicCurdServiceImpl<HelpDocViewRecordMapper, HelpDocViewRecordEntity> implements IHelpDocViewRecordService {

    @Override
    public Page<HelpDocViewRecordEntity> selectPage(HelpDocViewRecordQueryDTO dto) {
        LambdaQueryWrapper<HelpDocViewRecordEntity> lambda = new QueryWrapper<HelpDocViewRecordEntity>().lambda();
        Integer helpDocId = dto.getHelpDocId();
        if (ObjectUtil.isNotNull(helpDocId)) {
            lambda.eq(HelpDocViewRecordEntity::getHelpDocId, helpDocId);
        }
        Integer userId = dto.getUserId();
        if (ObjectUtil.isNotNull(userId)) {
            lambda.eq(HelpDocViewRecordEntity::getUserId, userId);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }
}
