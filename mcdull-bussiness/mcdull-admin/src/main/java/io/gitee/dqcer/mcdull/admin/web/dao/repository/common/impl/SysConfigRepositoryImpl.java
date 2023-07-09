package io.gitee.dqcer.mcdull.admin.web.dao.repository.common.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.common.SysConfigMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.common.ISysConfigRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.entity.MiddleDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
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
public class SysConfigRepositoryImpl extends ServiceImpl<SysConfigMapper, SysConfigDO> implements ISysConfigRepository {

    /**
     * 查询值键
     *
     * @param key 关键
     * @return {@link String}
     */
    @Override
    public SysConfigDO findByKey(String key) {
        LambdaQueryWrapper<SysConfigDO> query = Wrappers.lambdaQuery();
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        query.eq(SysConfigDO::getConfigKey, key);
        List<SysConfigDO> list = baseMapper.selectList(query);
        if (ObjUtil.isNotNull(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Page<SysConfigDO> selectPage(ConfigLiteDTO dto) {
        LambdaQueryWrapper<SysConfigDO> lambda = new QueryWrapper<SysConfigDO>().lambda();
        lambda.eq(SysConfigDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        String name = dto.getName();
        if (StrUtil.isNotBlank(name)) {
            lambda.like(SysConfigDO::getName, name);
        }
        String value = dto.getValue();
        if (StrUtil.isNotBlank(value)) {
            lambda.like(SysConfigDO::getConfigValue, value);
        }
        String configKey = dto.getConfigKey();
        if (StrUtil.isNotBlank(configKey)) {
            lambda.like(SysConfigDO::getConfigKey, configKey);
        }
        String configType = dto.getConfigType();
        if (StrUtil.isNotBlank(configType)) {
            lambda.eq(SysConfigDO::getConfigType, configType);
        }
        Date startTime = dto.getStartTime();
        Date endTime = dto.getEndTime();
        if (ObjUtil.isNotNull(startTime) && ObjUtil.isNotNull(endTime)) {
            lambda.between(MiddleDO::getCreatedTime, startTime, endTime);
        }
        lambda.orderByDesc(MiddleDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Override
    public List<SysConfigDO> getListByKey(String configKey) {
        LambdaQueryWrapper<SysConfigDO> query = Wrappers.lambdaQuery();
        query.eq(SysConfigDO::getConfigKey, configKey);
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        return baseMapper.selectList(query);
    }

    @Override
    public void removeUpdate(Long id) {
        LambdaUpdateWrapper<SysConfigDO> update = Wrappers.lambdaUpdate();
        update.set(BaseDO::getDelFlag, DelFlayEnum.DELETED.getCode());
        update.set(BaseDO::getDelBy, UserContextHolder.currentUserId());
        update.eq(IdDO::getId, id);
        baseMapper.update(null, update);
    }
}
