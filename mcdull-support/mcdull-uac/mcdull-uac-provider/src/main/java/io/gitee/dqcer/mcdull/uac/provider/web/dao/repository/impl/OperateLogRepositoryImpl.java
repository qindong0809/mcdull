package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ChangeLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OperateLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.OperateLogMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IOperateLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class OperateLogRepositoryImpl extends ServiceImpl<OperateLogMapper, OperateLogEntity>  implements IOperateLogRepository {

    /**
     * 按条件分页查询
     *
     * @param param 参数
     * @return {@link Page< OperateLogEntity >}
     */
    @Override
    public Page<OperateLogEntity> selectPage(OperateLogQueryDTO param) {
        LambdaQueryWrapper<OperateLogEntity> lambda = Wrappers.lambdaQuery();
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

}