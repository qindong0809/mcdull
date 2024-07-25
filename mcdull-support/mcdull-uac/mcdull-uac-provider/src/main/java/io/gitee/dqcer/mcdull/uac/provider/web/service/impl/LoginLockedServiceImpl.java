package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginFailQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLockedEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLockedVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ILoginLockedRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginLockedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Login Locked Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class LoginLockedServiceImpl
        extends BasicServiceImpl<ILoginLockedRepository> implements ILoginLockedService {


    @Override
    public LoginLockedEntity get(String loginName) {
        return baseRepository.get(loginName);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void lock(String loginName, Integer failedLoginMaximumTime) {
        LoginLockedEntity entity = new LoginLockedEntity();
        entity.setLockFlag(true);
        entity.setLoginName(loginName);
        Date date = new Date();
        entity.setLoginLockBeginTime(date);
        entity.setLoginLockEndTime(DateUtil.offsetMinute(date, failedLoginMaximumTime));
        entity.setLoginFailCount(1);
        baseRepository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void updateFailCount(LoginLockedEntity entity) {
        entity.setLoginFailCount(entity.getLoginFailCount() + 1);
        baseRepository.updateById(entity);
    }

    @Override
    public PagedVO<LoginLockedVO> queryPage(LoginFailQueryDTO dto) {
        Page<LoginLockedEntity> entityPage = baseRepository.selectPage(dto);
        List<LoginLockedVO> voList = new ArrayList<>();
        for (LoginLockedEntity entity : entityPage.getRecords()) {
            LoginLockedVO vo = this.convert(entity);
            voList.add(vo);
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unlock(List<Integer> idList) {
        List<LoginLockedEntity> list = baseRepository.listByIds(idList);
        if (list.size() != idList.size()) {
            this.throwDataNotExistException(idList);
        }
        for (LoginLockedEntity lockedEntity : list) {
            lockedEntity.setLockFlag(false);
        }
        baseRepository.updateBatchById(list);
    }

    private LoginLockedVO convert(LoginLockedEntity entity) {
        LoginLockedVO vo = new LoginLockedVO();
        vo.setLoginFailId(entity.getId());
        vo.setLoginName(entity.getLoginName());
        vo.setLoginFailCount(entity.getLoginFailCount());
        vo.setLockFlag(entity.getLockFlag());
        vo.setLoginLockBeginTime(LocalDateTimeUtil.of(entity.getLoginLockBeginTime()));
        vo.setLoginLockEndTime(LocalDateTimeUtil.of(entity.getLoginLockEndTime()));
        vo.setCreateTime(LocalDateTimeUtil.of(entity.getCreatedTime()));
        vo.setUpdateTime(LocalDateTimeUtil.of(entity.getUpdatedTime()));
        return vo;
    }

}
