package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ILoginLogRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginLogService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Login Log Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class LoginLogServiceImpl
        extends BasicServiceImpl<ILoginLogRepository> implements ILoginLogService {

    @Resource
    private IUserService userService;

    @Transactional(readOnly = true)
    @Override
    public PagedVO<LoginLogVO> queryByPage(LoginLogQueryDTO dto) {
        Integer userId = dto.getUserId();
        if (ObjUtil.isNotNull(userId)) {
            UserEntity user = userService.get(userId);
            if (ObjUtil.isNotNull(user)) {
                dto.setUserName(user.getLoginName());
            }
        }
        Page<LoginLogEntity> entityPage = baseRepository.selectPage(dto);
        List<LoginLogVO> voList = new ArrayList<>();
        List<LoginLogEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            for (LoginLogEntity entity : records) {
                LoginLogVO vo = this.convertToConfigVO(entity);
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void add(LoginLogEntity entity) {
        String remark = entity.getRemark();
        if (StrUtil.isNotBlank(remark)) {
            entity.setRemark(dynamicLocaleMessageSource.getMessage(remark));
        }
        baseRepository.insert(entity);
    }

    private LoginLogVO convertToConfigVO(LoginLogEntity entity) {
        LoginLogVO vo = new LoginLogVO();
        vo.setLoginLogId(entity.getId());
        vo.setUserId(entity.getId());
        vo.setLoginName(entity.getLoginName());
        vo.setLoginIp(entity.getLoginIp());
        vo.setLoginIpRegion(entity.getLoginIpRegion());
        vo.setUserAgent(entity.getUserAgent());
        vo.setRemark(entity.getRemark());
        vo.setLoginResult(entity.getLoginResult());
        vo.setCreateTime(entity.getCreatedTime());
        return vo;
    }
}
