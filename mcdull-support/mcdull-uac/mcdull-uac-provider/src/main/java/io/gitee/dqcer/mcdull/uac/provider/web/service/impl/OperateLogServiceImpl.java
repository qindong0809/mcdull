package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.NameValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OperateLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.OperateLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IOperateLogRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IOperateLogService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
        extends BasicServiceImpl<IOperateLogRepository> implements IOperateLogService {

    @Resource
    private IUserService userService;

    @Resource
    private IUserManager userManager;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void save(OperateLogEntity dto) {
        baseRepository.save(dto);
    }

    @Override
    public PagedVO<OperateLogVO> queryByPage(OperateLogQueryDTO dto) {
        String userName = dto.getUserName();
        List<Integer> userIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(userName)) {
            List<UserEntity> userList = userManager.getLike(userName);
            if (CollUtil.isEmpty(userList)) {
                return PageUtil.empty(dto);
            }
            userIdList = userList.stream().map(IdEntity::getId).collect(Collectors.toList());
        }
        Page<OperateLogEntity> entityPage = baseRepository.selectPage(dto, userIdList);
        List<OperateLogVO> voList = new ArrayList<>();
        List<OperateLogEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Set<Integer> userIdSet = records.stream().map(OperateLogEntity::getUserId).collect(Collectors.toSet());
            Map<Integer, UserEntity> userMap = userManager.getEntityMap(new ArrayList<>(userIdSet));
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
        OperateLogEntity entity = baseRepository.getById(operateLogId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(operateLogId);
        }
        OperateLogVO vo = this.convertToLogVO(entity);
        UserEntity user = userService.get(vo.getOperateUserId());
        if (ObjUtil.isNotNull(user)) {
            vo.setOperateUserName(user.getActualName());
        }
        return vo;
    }

    @Override
    public KeyValueVO<List<String>, List<Integer>> home() {
        List<String> key = new ArrayList<>();
        List<Integer> value = new ArrayList<>();
        List<Map<String, Object>> list = baseRepository.home();
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
        List<OperateLogEntity> list = baseRepository.getOnlyModule();
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
}
