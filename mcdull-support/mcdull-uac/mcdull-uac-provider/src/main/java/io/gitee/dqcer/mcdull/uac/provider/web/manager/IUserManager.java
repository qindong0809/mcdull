package io.gitee.dqcer.mcdull.uac.provider.web.manager;

import io.gitee.dqcer.mcdull.business.common.audit.Audit;

import java.util.List;
import java.util.Map;

/**
 * Audit service
 *
 * @author dqcer
 * @since 2024/7/25 9:19
 */

public interface IUserManager {

   /**
    * 获取名称映射
    *
    * @param userIdList 用户 ID 列表
    * @return {@link Map }<{@link Integer }, {@link String }>
    */
   Map<Integer, String> getNameMap(List<Integer> userIdList);

   /**
    * 按登录名获取名称映射
    *
    * @param loginList 登录列表
    * @return {@link Map }<{@link String }, {@link String }>
    */
   Map<String, String> getNameMapByLoginName(List<String> loginList);
}
