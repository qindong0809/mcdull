package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.LoginFailQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.LoginLockedEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.LoginLockedVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.ILoginLockedRepository;
import io.gitee.dqcer.mcdull.system.provider.web.service.ILoginLockedService;
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
            lockedEntity.setInactive(true);
        }
        baseRepository.updateBatchById(list, list.size());
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateFailCount(String username, Integer failedLoginMaximumTime, int failCount) {
        LoginLockedEntity entity = this.get(username);
        if (ObjUtil.isNull(entity)) {
             entity = new LoginLockedEntity();
            entity.setLoginName(username);
        }
        entity.setLockFlag(false);
        Date date = new Date();
        entity.setLoginLockBeginTime(date);
        entity.setLoginLockEndTime(DateUtil.offsetMinute(date, failedLoginMaximumTime));
        entity.setLoginFailCount(failCount);
        entity.setInactive(false);
        baseRepository.saveOrUpdate(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inactive(String username) {
         LoginLockedEntity entity = this.get(username);
        if (ObjUtil.isNotNull(entity)) {
            entity.setInactive(true);
        }
        baseRepository.updateById(entity);
    }

    @Override
    public int getLoginFailureCount(String loginName) {
         LoginLockedEntity lockedEntity = this.get(loginName);
        if (ObjUtil.isNotNull(lockedEntity)) {
            if (BooleanUtil.isFalse(lockedEntity.getInactive())) {
                Date loginLockEndTime = lockedEntity.getLoginLockEndTime();
                if (DateUtil.compare(loginLockEndTime, new Date()) > 0) {
                    return Convert.toInt(lockedEntity.getLoginFailCount(), 0);
                }
            }
        }
        return 0;
    }

    private LoginLockedVO convert(LoginLockedEntity entity) {
        LoginLockedVO vo = new LoginLockedVO();
        vo.setLoginFailId(entity.getId());
        vo.setLoginName(entity.getLoginName());
        vo.setLoginFailCount(entity.getLoginFailCount());
        vo.setLockFlag(entity.getLockFlag());
        vo.setLoginLockBeginTime(entity.getLoginLockBeginTime());
        vo.setLoginLockEndTime(entity.getLoginLockEndTime());
        vo.setCreateTime(entity.getCreatedTime());
        vo.setUpdateTime(entity.getUpdatedTime());
        return vo;
    }

}
