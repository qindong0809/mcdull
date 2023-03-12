package io.gitee.dqcer.mcdull.admin.web.dao.repository.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.common.SysConfigMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.common.ISysConfigRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.util.ObjUtil;
import org.springframework.stereotype.Service;

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
}
