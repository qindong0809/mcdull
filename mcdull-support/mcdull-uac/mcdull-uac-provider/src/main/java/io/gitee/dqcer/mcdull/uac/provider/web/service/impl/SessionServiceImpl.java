package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SessionQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.SessionVO;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ISessionService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;


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
    @Resource
    private ICommonManager commonManager;

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

    @Override
    public boolean exportData(SessionQueryDTO dto) {
        commonManager.exportExcel(new SessionQueryDTO(), this::queryPage, StrUtil.EMPTY, this.getTitleMap());
        return true;
    }

    private Map<String, Func1<SessionVO, ?>> getTitleMap() {
        Map<String, Func1<SessionVO, ?>> titleMap = new HashMap<>(8);
        titleMap.put("会话ID", SessionVO::getId);
        titleMap.put("登录名", SessionVO::getLoginName);
        titleMap.put("用户名", SessionVO::getActualName);
        titleMap.put("创建时间", SessionVO::getCreateTime);
        return titleMap;
    }
}
