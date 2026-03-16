package io.gitee.dqcer.mcdull.framework.security;

import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;

/**
 * StpLogic 门面类，管理项目中所有的 StpLogic 账号体系
 *
 * @author dqcer
 * @since 2025/08/28
 */
public class StpKit {

    /**
     * PCUser 业务端。默认原生会话对象
     */
    public static final StpLogic DEFAULT = StpUtil.stpLogic;

    /**
     * PCAdmin 业务管理端
     */
    public static final StpLogic ADMIN = new StpLogic("admin");

    /**
     * AppUser 移动业务端
     */
    public static final StpLogic APP_USER = new StpLogic("app_user");
}
