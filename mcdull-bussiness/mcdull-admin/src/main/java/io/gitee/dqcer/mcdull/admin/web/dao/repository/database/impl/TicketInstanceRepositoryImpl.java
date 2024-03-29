package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.database.TicketEntity;
import io.gitee.dqcer.mcdull.admin.model.entity.database.TicketInstanceEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.TicketInstanceMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.ITicketInstanceRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
*  数据库操作封装实现层
*
* @author dqcer
* @since 2023-08-17
*/
@Service
public class TicketInstanceRepositoryImpl extends ServiceImpl<TicketInstanceMapper, TicketInstanceEntity>  implements ITicketInstanceRepository {

    private static final Logger log = LoggerFactory.getLogger(TicketInstanceRepositoryImpl.class);

    /**
     * 根据ID列表批量查询数据
     *
     * @param idList id列表
     * @return {@link List< TicketEntity >}
     */
    @Override
    public List<TicketInstanceEntity> queryListByIds(List<Long> idList) {
        LambdaQueryWrapper<TicketInstanceEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TicketInstanceEntity::getId, idList);
        List<TicketInstanceEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link TicketEntity}
     */
    @Override
    public TicketInstanceEntity getById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    @Override
    public Long insert(TicketInstanceEntity entity) {
        int rowSize = baseMapper.insert(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, entity:{}", rowSize, entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    /**
     * 存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
    @Override
    public boolean exist(TicketInstanceEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public List<TicketInstanceEntity> getListByTicketId(Long ticketId) {
        LambdaQueryWrapper<TicketInstanceEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TicketInstanceEntity::getTicketId, ticketId);
        List<TicketInstanceEntity> list = baseMapper.selectList(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Long ticketId, Long groupId, List<Long> instanceList) {
        List<TicketInstanceEntity> list = new ArrayList<>(instanceList.size());
        for (Long instanceId : instanceList) {
            TicketInstanceEntity instanceDO = new TicketInstanceEntity();
            instanceDO.setInstanceId(instanceId);
            instanceDO.setTicketId(ticketId);
            instanceDO.setGroupId(groupId);
            list.add(instanceDO);
        }
        this.saveBatch(list);
    }

    /**
    * 根据id删除批处理
    *
    * @param ids id集
    */
    @Override
    public void deleteBatchByIds(List<Long> ids) {
        int rowSize = baseMapper.deleteBatchIds(ids);
        if (rowSize != ids.size()) {
            log.error("数据插入失败 actual: {}, plan: {}, ids: {}", rowSize, ids.size(), ids);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}