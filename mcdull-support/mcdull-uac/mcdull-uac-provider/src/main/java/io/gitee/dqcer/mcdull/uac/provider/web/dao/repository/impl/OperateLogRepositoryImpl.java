package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.TimestampEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OperateLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.OperateLogMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IOperateLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Operate Log Repository Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class OperateLogRepositoryImpl
        extends CrudRepository<OperateLogMapper, OperateLogEntity> implements IOperateLogRepository {

    @Override
    public Page<OperateLogEntity> selectPage(OperateLogQueryDTO param, List<Integer> userIdList) {
        LambdaQueryWrapper<OperateLogEntity> lambda = Wrappers.lambdaQuery();
        String startDate = param.getStartDate();
        String endDate = param.getEndDate();
        if (StrUtil.isAllNotBlank(startDate, endDate)) {
            lambda.between(TimestampEntity::getCreatedTime,
                    DateUtil.parseDate(startDate), DateUtil.endOfDay(DateUtil.parseDate(endDate)));
        }
        lambda.eq(ObjUtil.isNotNull(param.getUserId()), OperateLogEntity::getUserId, param.getUserId());
        if (CollUtil.isNotEmpty(userIdList)) {
            lambda.in(OperateLogEntity::getUserId, userIdList);
        }
        Boolean successFlag = param.getSuccessFlag();
        if (ObjUtil.isNotNull(successFlag)) {
            lambda.eq(OperateLogEntity::getSuccessFlag, successFlag);
        }
        String url = param.getUrl();
        if (StrUtil.isNotBlank(url)) {
            lambda.like(OperateLogEntity::getUrl, url);
        }
        String traceId = param.getTraceId();
        if (StrUtil.isNotBlank(traceId)) {
            lambda.like(OperateLogEntity::getTraceId, traceId);
        }
        String keyword = param.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            lambda.like(OperateLogEntity::getParam, keyword);
        }
        String module = param.getModule();
        if (StrUtil.isNotBlank(module)) {
            lambda.like(OperateLogEntity::getModule, module);
        }
        String content = param.getContent();
        if (StrUtil.isNotBlank(content)) {
            lambda.like(OperateLogEntity::getContent, content);
        }
        lambda.orderByDesc(ListUtil.of(TimestampEntity::getCreatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    @Override
    public List<Map<String, Object>> home() {
        QueryWrapper<OperateLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("date_format(created_time, '%Y-%m-%d %H:%i:%s') as 'createdTime',count(1) as 'count'")
                .groupBy("date_format(created_time, '%Y-%m-%d %H:%i:%s')");
        return this.listMaps(queryWrapper);
    }

    @Override
    public List<OperateLogEntity> getOnlyModule() {
        LambdaQueryWrapper<OperateLogEntity> query = Wrappers.lambdaQuery();
        query.select(OperateLogEntity::getModule);
        return baseMapper.selectList(query);
    }

}