package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserPostEntity;

import java.util.List;

/**
 * 用户部门 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IUserPostRepository extends IService<UserPostEntity> {

    /**
     * 通过用户id获取角色id列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link Long}>
     */
    List<Long> listPostByUserId(Long userId);

    /**
     * 更新根据用户id
     *
     * @param id      id
     * @param roleIds 角色id
     */
    void updateByUserId(Long id, List<Long> roleIds);
}
