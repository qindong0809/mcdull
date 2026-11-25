package io.gitee.dqcer.mcdull.system.provider.web.manager;

import java.util.Map;

/**
 * 管理端用户只读查询契约。
 *
 * <p>供 AdminDeptService 等被依赖方补齐创建人姓名，
 * 避免反向注入 IAdminUserService 形成循环。
 */
public interface IAdminUserQueryManager {

    Map<Integer, String> getUserNameMap();
}
