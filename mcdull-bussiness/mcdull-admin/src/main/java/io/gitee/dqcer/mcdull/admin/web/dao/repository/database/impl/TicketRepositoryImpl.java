package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.database.TicketAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.TicketEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.TicketMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.ITicketRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
*  数据库操作封装实现层
*
* @author dqcer
* @since 2023-08-17
*/
@Service
public class TicketRepositoryImpl extends ServiceImpl<TicketMapper, TicketEntity>  implements ITicketRepository {

    private static final Logger log = LoggerFactory.getLogger(TicketRepositoryImpl.class);

    /**
     * 根据ID列表批量查询数据
     *
     * @param idList id列表
     * @return {@link List< TicketEntity >}
     */
    @Override
    public List<TicketEntity> queryListByIds(List<Long> idList) {
        LambdaQueryWrapper<TicketEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TicketEntity::getId, idList);
        List<TicketEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 按条件分页查询
     *
     * @param param 参数
     * @return {@link Page< TicketEntity >}
     */
    @Override
    public Page<TicketEntity> selectPage(TicketAddDTO param) {
        LambdaQueryWrapper<TicketEntity> lambda = new QueryWrapper<TicketEntity>().lambda();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            // TODO 组装查询条件
        }
        lambda.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(param.getCurrentPage(), param.getPageSize()), lambda);
    }

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link TicketEntity}
     */
    @Override
    public TicketEntity getById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    @Override
    public Long insert(TicketEntity entity) {
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
    public boolean exist(TicketEntity entity) {
        LambdaQueryWrapper<TicketEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TicketEntity::getName, entity.getName());
        wrapper.ne(ObjUtil.isNotNull(entity.getId()), IdEntity::getId,entity.getId());
        return !baseMapper.selectList(wrapper).isEmpty();
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