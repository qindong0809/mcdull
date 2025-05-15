package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.system.provider.model.dto.EnterpriseQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.OaEnterpriseEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.OaEnterpriseMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IOaEnterpriseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Enterprise Repository Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class OaEnterpriseRepositoryImpl
        extends CrudRepository<OaEnterpriseMapper, OaEnterpriseEntity> implements IOaEnterpriseRepository {

    @Override
    public Page<OaEnterpriseEntity> selectPage(EnterpriseQueryDTO dto) {
        LambdaQueryWrapper<OaEnterpriseEntity> lambda = Wrappers.lambdaQuery();
        String keywords = dto.getKeywords();
        if (StrUtil.isNotBlank(keywords)) {
            lambda.and(i->i.like(OaEnterpriseEntity::getEnterpriseName, keywords)
                    .or().like(OaEnterpriseEntity::getContact, keywords)
                    .or().like(OaEnterpriseEntity::getContactPhone, keywords));
        }
        LocalDate startTime = dto.getStartTime();
        LocalDate endTime = dto.getEndTime();
        if (ObjUtil.isAllNotEmpty(startTime, endTime)) {
            lambda.between(RelEntity::getCreatedTime, startTime,
                    LocalDateTimeUtil.endOfDay(endTime.atStartOfDay()));
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }
}