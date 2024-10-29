package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.LoginLogMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ILoginLogRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Login log repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class LoginLogRepositoryImpl
        extends CrudRepository<LoginLogMapper, LoginLogEntity> implements ILoginLogRepository {

    @Override
    public List<LoginLogEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<LoginLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(LoginLogEntity::getId, idList);
        List<LoginLogEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public Page<LoginLogEntity> selectPage(LoginLogQueryDTO param) {
        LambdaQueryWrapper<LoginLogEntity> lambda = new QueryWrapper<LoginLogEntity>().lambda();
        lambda.like(ObjUtil.isNotNull(param.getKeyword()), LoginLogEntity::getLoginName, param.getKeyword());
        lambda.like(ObjUtil.isNotNull(param.getIp()), LoginLogEntity::getLoginIp, param.getIp());
        lambda.eq(StrUtil.isNotBlank(param.getUserName()), LoginLogEntity::getLoginName, param.getUserName());
        Date startDate = param.getStartDate();
        Date endDate = param.getEndDate();
        if (ObjUtil.isNotNull(startDate) && ObjUtil.isNotNull(endDate)) {
            lambda.between(LoginLogEntity::getCreatedTime, startDate, endDate);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    @Override
    public LoginLogEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Integer insert(LoginLogEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public boolean exist(LoginLogEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }
}