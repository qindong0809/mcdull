package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.database.TicketAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.TicketDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.TicketMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.ITicketRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.entity.RelDO;
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
public class TicketRepositoryImpl extends ServiceImpl<TicketMapper, TicketDO>  implements ITicketRepository {

    private static final Logger log = LoggerFactory.getLogger(TicketRepositoryImpl.class);

    /**
     * 根据ID列表批量查询数据
     *
     * @param idList id列表
     * @return {@link List<TicketDO>}
     */
    @Override
    public List<TicketDO> queryListByIds(List<Long> idList) {
        LambdaQueryWrapper<TicketDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TicketDO::getId, idList);
        List<TicketDO> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 按条件分页查询
     *
     * @param param 参数
     * @return {@link Page<TicketDO>}
     */
    @Override
    public Page<TicketDO> selectPage(TicketAddDTO param) {
        LambdaQueryWrapper<TicketDO> lambda = new QueryWrapper<TicketDO>().lambda();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            // TODO 组装查询条件
        }
        lambda.orderByDesc(RelDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link TicketDO}
     */
    @Override
    public TicketDO getById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    @Override
    public Long insert(TicketDO entity) {
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
    public boolean exist(TicketDO entity) {
        LambdaQueryWrapper<TicketDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TicketDO::getName, entity.getName());
        wrapper.ne(ObjUtil.isNotNull(entity.getId()), IdDO::getId,entity.getId());
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