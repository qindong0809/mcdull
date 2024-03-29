package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.framework.base.vo.TreeSelectVO;
import io.gitee.dqcer.mcdull.framework.base.support.VO;

import java.util.List;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
public class RoleMenuTreeSelectVO implements VO {

   private List<Long> checkedKeys;

   private List<TreeSelectVO> menus;

   public List<Long> getCheckedKeys() {
      return checkedKeys;
   }

   public RoleMenuTreeSelectVO setCheckedKeys(List<Long> checkedKeys) {
      this.checkedKeys = checkedKeys;
      return this;
   }

   public List<TreeSelectVO> getMenus() {
      return menus;
   }

   public RoleMenuTreeSelectVO setMenus(List<TreeSelectVO> menus) {
      this.menus = menus;
      return this;
   }
}
