package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EnterpriseQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OaEnterpriseEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.OaEnterpriseMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IOaEnterpriseRepository;
import org.springframework.stereotype.Service;

/**
* 系统配置 数据库操作封装实现层
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class OaEnterpriseRepositoryImpl
        extends ServiceImpl<OaEnterpriseMapper, OaEnterpriseEntity>  implements IOaEnterpriseRepository {


    @Override
    public Page<OaEnterpriseEntity> selectPage(EnterpriseQueryDTO dto) {
        LambdaQueryWrapper<OaEnterpriseEntity> lambda = Wrappers.lambdaQuery();
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }
}