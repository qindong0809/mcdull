package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SessionQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.SessionVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ISessionService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Session Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class SessionServiceImpl implements ISessionService {

    @Resource
    private IUserService userService;

    @Override
    public PagedVO<SessionVO> queryPage(SessionQueryDTO dto) {
        List<String> sessionIdList = StpUtil
                .searchSessionId("", 0, -1, false);
        if (sessionIdList.isEmpty()) {
            return PageUtil.empty(dto);
        }
        List<SessionVO> list = new ArrayList<>();
        for (String sessionId : sessionIdList) {
            SaSession session = StpUtil.getSessionBySessionId(sessionId);
            if (ObjUtil.isNotNull(session)) {
                Object loginId = session.getLoginId();
                SessionVO vo = new SessionVO();
                vo.setId(session.getId());
                vo.setLoginId(Convert.toInt(loginId.toString()));
                vo.setCreateTime(new Date(session.getCreateTime()));
                UserEntity user = userService.get(vo.getLoginId());
                if (ObjUtil.isNotNull(user)) {
                    vo.setActualName(user.getActualName());
                    vo.setLoginName(user.getLoginName());
                }
                list.add(vo);
            }
        }
        return PageUtil.of(list, dto);
    }

    @Override
    public void batchKickout(List<Integer> loginIdList) {
        for (Integer userId : loginIdList) {
            StpUtil.kickout(userId);
        }
    }
}
