package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * user manager service
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

   /**
    * get like
    *
    * @param userName 用户名
    * @return {@link List }<{@link UserEntity }>
    */
   List<UserEntity> getLike(String userName);

   /**
    * 获取实体映射
    *
    * @param userIdList 用户 ID 列表
    * @return {@link Map }<{@link Integer }, {@link UserEntity }>
    */
   Map<Integer, UserEntity> getEntityMap(List<Integer> userIdList);

   /**
    * 获取用户部门
    *
    * @param userIdList 用户 ID 列表
    * @return {@link Map }<{@link Integer }, {@link Integer }>
    */
   Map<Integer, Integer> getUserDepartmentMap(List<Integer> userIdList);

   /**
    * 获取负责人列表
    *
    * @return {@link List }<{@link LabelValueVO }<{@link Integer }, {@link String }>>
    */
   List<LabelValueVO<Integer, String>> getResponsibleList();

   /**
    * 获取用户 ID 列表
    *
    * @param departmentId 部门 ID
    * @return {@link List }<{@link Integer }>
    */
   List<Integer> getUserIdList(Integer departmentId);

   /**
    * 获取地图
    *
    * @param list 列表
    * @return {@link Map }<{@link Integer }, {@link String }>
    */
   Map<Integer, String> getMap(List<? extends BaseEntity<Integer>> list);
}
