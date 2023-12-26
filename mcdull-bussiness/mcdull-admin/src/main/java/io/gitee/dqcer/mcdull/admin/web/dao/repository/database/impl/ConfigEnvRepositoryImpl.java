package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.database.ConfigEnvDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.ConfigEnvMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IConfigEnvRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
*  数据库操作封装实现层
*
* @author dqcer
* @since 2023-08-29
*/
@Service
public class ConfigEnvRepositoryImpl extends ServiceImpl<ConfigEnvMapper, ConfigEnvDO>  implements IConfigEnvRepository {

    private static final Logger log = LoggerFactory.getLogger(ConfigEnvRepositoryImpl.class);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ConfigEnvDO}
     */
    @Override
    public ConfigEnvDO getById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    @Override
    public Long insert(ConfigEnvDO entity) {
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
    public boolean exist(ConfigEnvDO entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public ConfigEnvDO getByType(Integer type) {
        LambdaQueryWrapper<ConfigEnvDO> query = Wrappers.lambdaQuery();
        query.eq(ConfigEnvDO::getType, type);
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        return baseMapper.selectOne(query);
    }
}