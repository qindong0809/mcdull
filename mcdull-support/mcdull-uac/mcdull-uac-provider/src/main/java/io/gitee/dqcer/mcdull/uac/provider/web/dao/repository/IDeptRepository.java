package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DeptDO;

import java.util.List;

/**
 * @author dqcer
 * @since 2022/12/26
 */
public interface IDeptRepository extends IService<DeptDO> {


    Integer insert(DeptDO entity);

    boolean delete(Integer id, String reason);

    List<DeptDO> listByParentId(Integer parentId);
}
/***
 * menus:
 *   hshome: 首页
 *   hslogin: 登录
 *   hsempty: 无Layout页
 *   hstable: 表格
 *   hssysManagement: 系统管理
 *   hsUser: 用户管理
 *   hsRole: 角色管理
 *   hsSystemMenu: 菜单管理
 *   hsDept: 部门管理
 */
