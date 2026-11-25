package io.gitee.dqcer.mcdull.system.provider.web.manager;

import java.util.List;

/**
 * 菜单只读查询契约。
 *
 * <p>供 FolderService / CommonManager 等被依赖方获取菜单路径，
 * 避免反向注入 MenuService 形成循环。
 */
public interface IMenuQueryManager {

    List<String> getCurrentMenuName();
}
