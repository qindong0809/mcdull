package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.TimestampEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.NameValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.OperateLogEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.OperateLogVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.OperateLogMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IOperateLogService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Operate Log Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class OperateLogServiceImpl
        extends BasicCurdServiceImpl<OperateLogMapper, OperateLogEntity> implements IOperateLogService {

    @Resource
    private IUserService userService;
    @Resource
    private ICommonManager commonManager;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean save(OperateLogEntity dto) {
        return super.save(dto);
    }

    @Override
    public PagedVO<OperateLogVO> queryByPage(OperateLogQueryDTO dto) {
        String userName = dto.getUserName();
        List<Integer> userIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(userName)) {
            List<UserEntity> userList = userService.getLike(userName);
            if (CollUtil.isEmpty(userList)) {
                return PageUtil.empty(dto);
            }
            userIdList = userList.stream().map(IdEntity::getId).collect(Collectors.toList());
        }
        Page<OperateLogEntity> entityPage = this.selectPage(dto, userIdList);
        List<OperateLogVO> voList = new ArrayList<>();
        List<OperateLogEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Set<Integer> userIdSet = records.stream().map(OperateLogEntity::getUserId).collect(Collectors.toSet());
            Map<Integer, UserEntity> userMap = userService.getEntityMap(new ArrayList<>(userIdSet));
            for (OperateLogEntity entity : records) {
                OperateLogVO vo = this.convertToLogVO(entity);
                Integer operateUserId = vo.getOperateUserId();
                UserEntity user = userMap.get(operateUserId);
                if (ObjUtil.isNotNull(user)) {
                    vo.setOperateUserName(user.getActualName());
                }
                voList.add(vo);
            }
        }

        return PageUtil.toPage(voList, entityPage);
    }

    private OperateLogVO convertToLogVO(OperateLogEntity entity) {
        OperateLogVO vo = new OperateLogVO();
        vo.setOperateLogId(entity.getId());
        vo.setOperateUserId(entity.getUserId());
        vo.setTraceId(entity.getTraceId());
        vo.setTimeTaken(entity.getTimeTaken());
        vo.setModule(entity.getModule());
        vo.setContent(entity.getContent());
        vo.setUrl(entity.getUrl());
        vo.setMethod(entity.getMethod());
        vo.setParam(entity.getParam());
        vo.setIp(entity.getIp());
        vo.setIpRegion(entity.getIpRegion());
        vo.setUserAgent(entity.getUserAgent());
        vo.setSuccessFlag(entity.getSuccessFlag());
        vo.setFailReason(entity.getFailReason());
        vo.setCreateTime(entity.getCreatedTime());
        return vo;
    }

    @Override
    public OperateLogVO detail(Integer operateLogId) {
        OperateLogEntity entity = super.mustGet(operateLogId);
        OperateLogVO vo = this.convertToLogVO(entity);
        UserEntity user = userService.get(vo.getOperateUserId());
        if (ObjUtil.isNotNull(user)) {
            vo.setOperateUserName(user.getActualName());
        }
        return vo;
    }

    @Override
    public KeyValueVO<List<String>, List<Integer>> homePie() {
        List<String> key = new ArrayList<>();
        List<Integer> value = new ArrayList<>();
        List<Map<String, Object>> list = this.home();
        for (Map<String, Object> map : list) {
            Object o = map.get("createdTime");
            key.add(Convert.toStr(o));
            Object count = map.get("count");
            Integer anInt = Convert.toInt(count);
            value.add(anInt);
        }
        return new KeyValueVO<>(key, value);
    }

    @Override
    public List<NameValueVO<String, Integer>> pieHome() {
        List<OperateLogEntity> list = this.getOnlyModule();
        if (CollUtil.isNotEmpty(list)) {
            List<NameValueVO<String, Integer>> voList = new ArrayList<>();
            Map<String, Long> map = list.stream()
                    .collect(Collectors.groupingBy(OperateLogEntity::getModule, Collectors.counting()));
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                voList.add(new NameValueVO<>(entry.getKey(), Convert.toInt(entry.getValue())));
            }
            return voList;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean exportData(OperateLogQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryByPage, StrUtil.EMPTY, this.getTitleList());
        return true;
    }

    private List<Pair<String, Func1<OperateLogVO, ?>>> getTitleList() {
        return Arrays.asList(
                Pair.of("用户", OperateLogVO::getOperateUserName),
                Pair.of("操作模块", OperateLogVO::getModule),
                Pair.of("操作内容", OperateLogVO::getContent),
                Pair.of("请求路径", OperateLogVO::getUrl),
                Pair.of("IP", OperateLogVO::getIp),
                Pair.of("IP地区", OperateLogVO::getIpRegion),
                Pair.of("客户端", OperateLogVO::getUserAgent),
                Pair.of("请求方法", OperateLogVO::getMethod),
                Pair.of("耗时(ms)", OperateLogVO::getTimeTaken),
                Pair.of("链路标识", OperateLogVO::getTraceId),
                Pair.of("请求结果", OperateLogVO::getSuccessFlag),
                Pair.of("时间", OperateLogVO::getCreateTime)
        );
    }


    public Page<OperateLogEntity> selectPage(OperateLogQueryDTO param, List<Integer> userIdList) {
        LambdaQueryWrapper<OperateLogEntity> lambda = Wrappers.lambdaQuery();
        String startDate = param.getStartDate();
        String endDate = param.getEndDate();
        if (CharSequenceUtil.isAllNotBlank(startDate, endDate)) {
            lambda.between(TimestampEntity::getCreatedTime,
                DateUtil.parseDate(startDate), DateUtil.endOfDay(DateUtil.parseDate(endDate)));
        }
        lambda.eq(ObjectUtil.isNotNull(param.getUserId()), OperateLogEntity::getUserId, param.getUserId());
        if (CollUtil.isNotEmpty(userIdList)) {
            lambda.in(OperateLogEntity::getUserId, userIdList);
        }
        Boolean successFlag = param.getSuccessFlag();
        if (ObjectUtil.isNotNull(successFlag)) {
            lambda.eq(OperateLogEntity::getSuccessFlag, successFlag);
        }
        String url = param.getUrl();
        if (CharSequenceUtil.isNotBlank(url)) {
            lambda.like(OperateLogEntity::getUrl, url);
        }
        String traceId = param.getTraceId();
        if (CharSequenceUtil.isNotBlank(traceId)) {
            lambda.like(OperateLogEntity::getTraceId, traceId);
        }
        String keyword = param.getKeyword();
        if (CharSequenceUtil.isNotBlank(keyword)) {
            lambda.like(OperateLogEntity::getParam, keyword);
        }
        String module = param.getModule();
        if (CharSequenceUtil.isNotBlank(module)) {
            lambda.like(OperateLogEntity::getModule, module);
        }
        String content = param.getContent();
        if (CharSequenceUtil.isNotBlank(content)) {
            lambda.like(OperateLogEntity::getContent, content);
        }
        lambda.orderByDesc(ListUtil.of(TimestampEntity::getCreatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    public List<Map<String, Object>> home() {
        QueryWrapper<OperateLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("date_format(created_time, '%Y-%m-%d %H:%i:%s') as 'createdTime',count(1) as 'count'")
            .groupBy("date_format(created_time, '%Y-%m-%d %H:%i:%s')");
        return this.listMaps(queryWrapper);
    }

    public List<OperateLogEntity> getOnlyModule() {
        LambdaQueryWrapper<OperateLogEntity> query = Wrappers.lambdaQuery();
        query.select(OperateLogEntity::getModule);
        return baseMapper.selectList(query);
    }


}
