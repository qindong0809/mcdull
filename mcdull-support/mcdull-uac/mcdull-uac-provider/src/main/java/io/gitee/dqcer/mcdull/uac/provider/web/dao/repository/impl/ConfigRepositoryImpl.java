package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.ConfigMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* 系统配置 数据库操作封装实现层
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class ConfigRepositoryImpl extends ServiceImpl<ConfigMapper, ConfigEntity>  implements IConfigRepository {

    private static final Logger log = LoggerFactory.getLogger(ConfigRepositoryImpl.class);

    /**
     * 根据ID列表批量查询数据
     *
     * @param idList id列表
     * @return {@link List< ConfigEntity >}
     */
    @Override
    public List<ConfigEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<ConfigEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ConfigEntity::getId, idList);
        List<ConfigEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 按条件分页查询
     *
     * @param param 参数
     * @return {@link Page< ConfigEntity >}
     */
    @Override
    public Page<ConfigEntity> selectPage(ConfigQueryDTO param) {
        LambdaQueryWrapper<ConfigEntity> lambda = new QueryWrapper<ConfigEntity>().lambda();
        String configKey = param.getConfigKey();
        if (StrUtil.isNotBlank(configKey)) {
            lambda.like(ConfigEntity::getConfigKey, configKey);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ConfigEntity}
     */
    @Override
    public ConfigEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Integer id
     */
    @Override
    public Integer insert(ConfigEntity entity) {
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
    public boolean exist(ConfigEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public ConfigEntity selectOne(String key) {
        LambdaQueryWrapper<ConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(ConfigEntity::getConfigKey, key);
        return baseMapper.selectOne(query);
    }

    /**
    * 根据id删除批处理
    *
    * @param ids id集
    */
    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        int rowSize = baseMapper.deleteBatchIds(ids);
        if (rowSize != ids.size()) {
            log.error("数据插入失败 actual: {}, plan: {}, ids: {}", rowSize, ids.size(), ids);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}