package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.database.InstanceBackDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.InstanceBackMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IInstanceBackRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
* Instance back 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class InstanceBackRepositoryImpl extends ServiceImpl<InstanceBackMapper, InstanceBackDO>  implements IInstanceBackRepository {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<InstanceBackDO> listByTicketId(Long ticketId) {
        LambdaQueryWrapper<InstanceBackDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(InstanceBackDO::getModel, InstanceBackDO.MODEL_TICKET);
        wrapper.eq(InstanceBackDO::getBizId, ticketId);
        wrapper.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        List<InstanceBackDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        return Collections.emptyList();
    }
}