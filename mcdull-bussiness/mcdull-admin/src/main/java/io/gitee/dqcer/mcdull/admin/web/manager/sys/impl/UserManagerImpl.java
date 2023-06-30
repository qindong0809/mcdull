package io.gitee.dqcer.mcdull.admin.web.manager.sys.impl;

import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.UserConvert;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DeptDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.PostDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.*;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IUserManager;
import io.gitee.dqcer.mcdull.framework.base.vo.BaseVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户通用逻辑实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserManagerImpl implements IUserManager {

    @Resource
    private IUserRoleRepository userRoleRepository;

    @Resource
    private IRoleRepository roleRepository;

    @Resource
    private IUserPostRepository userPostRepository;

    @Resource
    private IPostRepository postRepository;

    @Resource
    private IDeptRepository deptRepository;

    /**
     * entity 转 VO
     *
     * @param entity 实体
     * @return {@link UserVO}
     */
    @Override
    public UserVO entityToVo(UserDO entity) {
        UserVO vo = UserConvert.entityToVO(entity);
        if (vo == null) {
            return null;
        }
        DeptDO deptDO = deptRepository.getById(vo.getDeptId());
        vo.setDeptName(deptDO.getName());
        List<BaseVO<Long, String>> baseRoles = this.getBaseRoles(vo.getId());
        vo.setRoles(baseRoles);
        return vo;
    }

    @Override
    public UserDetailVO entityToDetailVo(UserDO userDO) {
        UserDetailVO vo = UserConvert.convertToUserDetailVO(userDO);
        DeptDO deptDO = deptRepository.getById(vo.getDeptId());
        vo.setDeptName(deptDO.getName());

        List<BaseVO<Long, String>> baseRoles = this.getBaseRoles(vo.getId());
        if (CollUtil.isNotEmpty(baseRoles)) {
            vo.setRoleIds(baseRoles.stream().map(BaseVO::getId).collect(Collectors.toList()));
        }
        List<BaseVO<Long, String>> basePosts = this.getBasePosts(vo.getId());
        if (CollUtil.isNotEmpty(basePosts)) {
            vo.setPostIds(basePosts.stream().map(BaseVO::getId).collect(Collectors.toList()));
        }
        return vo;
    }

    private List<BaseVO<Long, String>> getBasePosts(Long id) {
        List<BaseVO<Long, String>> baseRoles = new ArrayList<>();
        List<Long> list = userPostRepository.listPostByUserId(id);
        if (!list.isEmpty()) {
            for (PostDO postDO : postRepository.listByIds(list)) {
                BaseVO<Long, String> role = new BaseVO<>();
                role.setId(postDO.getId());
                role.setName(postDO.getPostName());
                baseRoles.add(role);
            }
        }
        return baseRoles;
    }

    private List<BaseVO<Long, String>> getBaseRoles(Long vo) {
        List<BaseVO<Long, String>> baseRoles = new ArrayList<>();
        List<Long> list = userRoleRepository.listRoleByUserId(vo);
        if (!list.isEmpty()) {
            for (RoleDO roleDO : roleRepository.listByIds(list)) {
                BaseVO<Long, String> role = new BaseVO<>();
                role.setId(roleDO.getId());
                role.setName(roleDO.getName());
                baseRoles.add(role);
            }
        }
        return baseRoles;
    }

    /**
     * 得到用户角色
     *
     * @param userId 用户id
     * @return {@link List}<{@link RoleDO}>
     */
    @Override
    public List<RoleDO> getUserRoles(Long userId) {
        List<Long> roleIds = userRoleRepository.listRoleByUserId(userId);
        if (!roleIds.isEmpty()) {
            List<RoleDO> roleDOList = roleRepository.listByIds(roleIds);
            if (!roleDOList.isEmpty()) {
                return roleDOList;
            }
        }
        return Collections.emptyList();
    }
}
