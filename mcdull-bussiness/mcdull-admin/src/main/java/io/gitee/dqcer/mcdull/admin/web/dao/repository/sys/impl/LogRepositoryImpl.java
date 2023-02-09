package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.LogDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.LogMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.ILogRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* 日志记录 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class LogRepositoryImpl extends ServiceImpl<LogMapper, LogDO>  implements ILogRepository {

    private static final Logger log = LoggerFactory.getLogger(LogRepositoryImpl.class);

    /**
     * 根据ID列表批量查询数据
     *
     * @param idList id列表
     * @return {@link List<LogDO>}
     */
    @Override
    public List<LogDO> queryListByIds(List<Long> idList) {
        LambdaQueryWrapper<LogDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(LogDO::getId, idList);
        List<LogDO> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 按条件分页查询
     *
     * @param param 参数
     * @return {@link Page<LogDO>}
     */
    @Override
    public Page<LogDO> selectPage(LogLiteDTO param) {
        LambdaQueryWrapper<LogDO> lambda = new QueryWrapper<LogDO>().lambda();
        String keyword = param.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            //TODO 组装查询条件
        }
        return baseMapper.selectPage(new Page<>(param.getPage(), param.getPageSize()), lambda);
    }

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link LogDO}
     */
    @Override
    public LogDO getById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    @Override
    public Long insert(LogDO entity) {
        int rowSize = baseMapper.insert(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            log.error("数据插入失败 rowSize: {}, entity:{}", rowSize, entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    /**
     * 通过主键删除
     *
     * @param id 主键
     * @return int 受影响的行数
     */
    @Override
    public int deleteById(Long id) {
        return baseMapper.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 主键集
     * @return int 受影响的行数
     */
    @Override
    public int deleteBatchIds(List<Long> ids) {
         return baseMapper.deleteBatchIds(ids);
    }

    /**
     * 存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
    @Override
    public boolean exist(LogDO entity) {
        List<LogDO> list = baseMapper.selectList(Wrappers.lambdaQuery(entity));
        return list.isEmpty();
    }
}