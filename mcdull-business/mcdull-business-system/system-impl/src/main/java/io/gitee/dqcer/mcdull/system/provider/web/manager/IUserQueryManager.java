package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户只读查询契约。
 *
 * <p>只提供展示数据补齐所需的查询能力，依赖止于 UserMapper，
 * 避免 CommonManager / FileService 等被依赖方反向注入 IUserService 形成循环。
 */
public interface IUserQueryManager {

    List<UserEntity> getLike(String userName);

    Map<Integer, String> getMap(List<? extends BaseEntity<Integer>> list);

    Map<Integer, String> getNameMap(List<Integer> userIdList);

    Map<String, String> getNameMapByLoginName(List<String> loginList);

    Map<Integer, UserEntity> getEntityMap(List<Integer> userIdList);

    UserEntity get(Integer userId);

    List<UserEntity> listByDeptList(List<Integer> deptIdList);
}
