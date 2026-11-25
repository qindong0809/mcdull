package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteUserDataSourceVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户数据源契约定义：业务服务按已认证用户 ID 查询其绑定的数据库。
 * <p>
 * 用户 ID 必须由服务端从会话取得，禁止透传客户端请求参数作为身份依据。
 *
 * @author dqcer
 */
public interface UserDataSourceApiDef {

    /**
     * 按用户 ID 查询绑定的数据源；用户未绑定时返回 data=null（用 unwrapOrNull 消费）。
     */
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/user-data-source/by-user")
    InnerResult<RemoteUserDataSourceVO> getByUserId(@RequestParam("userId") String userId);
}
