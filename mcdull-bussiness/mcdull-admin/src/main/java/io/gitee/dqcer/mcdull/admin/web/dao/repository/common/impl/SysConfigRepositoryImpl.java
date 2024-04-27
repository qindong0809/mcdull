package io.gitee.dqcer.mcdull.admin.web.dao.repository.common.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.common.SysConfigMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.common.ISysConfigRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 系统配置 数据库操作封装实现层
 *
 * @author dqcer
 * @since  2022/12/25
 */
@Service
public class SysConfigRepositoryImpl extends ServiceImpl<SysConfigMapper, SysConfigEntity> implements ISysConfigRepository {

    /**
     * 查询值键
     *
     * @param key 关键
     * @return {@link String}
     */
    @Override
    public SysConfigEntity findByKey(String key) {
        LambdaQueryWrapper<SysConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(SysConfigEntity::getConfigKey, key);
        List<SysConfigEntity> list = baseMapper.selectList(query);
        if (ObjUtil.isNotNull(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Page<SysConfigEntity> selectPage(ConfigLiteDTO dto) {
        LambdaQueryWrapper<SysConfigEntity> lambda = new QueryWrapper<SysConfigEntity>().lambda();
        String name = dto.getName();
        if (StrUtil.isNotBlank(name)) {
            lambda.like(SysConfigEntity::getName, name);
        }
        String value = dto.getValue();
        if (StrUtil.isNotBlank(value)) {
            lambda.like(SysConfigEntity::getConfigValue, value);
        }
        String configKey = dto.getConfigKey();
        if (StrUtil.isNotBlank(configKey)) {
            lambda.like(SysConfigEntity::getConfigKey, configKey);
        }
        String configType = dto.getConfigType();
        if (StrUtil.isNotBlank(configType)) {
            lambda.eq(SysConfigEntity::getConfigType, configType);
        }
        Date startTime = dto.getStartTime();
        Date endTime = dto.getEndTime();
        if (ObjUtil.isNotNull(startTime) && ObjUtil.isNotNull(endTime)) {
            lambda.between(RelEntity::getCreatedTime, startTime, endTime);
        }
        lambda.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Override
    public List<SysConfigEntity> getListByKey(String configKey) {
        LambdaQueryWrapper<SysConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(SysConfigEntity::getConfigKey, configKey);
        return baseMapper.selectList(query);
    }

    @Override
    public void removeUpdate(Long id) {
        baseMapper.deleteById(id);
    }
}
