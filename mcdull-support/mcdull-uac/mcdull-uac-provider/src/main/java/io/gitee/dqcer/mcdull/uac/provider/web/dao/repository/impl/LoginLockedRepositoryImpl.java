package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginFailQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLockedEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.LoginLockedMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ILoginLockedRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
* @author dqcer
* @since 2024-04-29
*/
@Service
public class LoginLockedRepositoryImpl
        extends ServiceImpl<LoginLockedMapper, LoginLockedEntity>  implements ILoginLockedRepository {


    @Override
    public LoginLockedEntity get(String loginName) {
        LambdaQueryWrapper<LoginLockedEntity> query = Wrappers.lambdaQuery();
        query.eq(LoginLockedEntity::getLoginName, loginName);
        List<LoginLockedEntity> list = baseMapper.selectList(query);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Page<LoginLockedEntity> selectPage(LoginFailQueryDTO param) {
        LambdaQueryWrapper<LoginLockedEntity> lambda = Wrappers.lambdaQuery();
        Boolean lockFlag = param.getLockFlag();
        if (ObjUtil.isNotNull(lockFlag)) {
            lambda.eq(LoginLockedEntity::getLockFlag, lockFlag);
        }
        LocalDate startDate = param.getLoginLockBeginTimeBegin();
        LocalDate endDate = param.getLoginLockBeginTimeBegin();
        if (ObjUtil.isAllNotEmpty(startDate)) {
            lambda.between(LoginLockedEntity::getLoginLockBeginTime, startDate, LocalDateTimeUtil.endOfDay(endDate.atStartOfDay()));
        }
        String loginName = param.getLoginName();
        if (StrUtil.isNotBlank(loginName)) {
            lambda.like(LoginLockedEntity::getLoginName, loginName);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }
}