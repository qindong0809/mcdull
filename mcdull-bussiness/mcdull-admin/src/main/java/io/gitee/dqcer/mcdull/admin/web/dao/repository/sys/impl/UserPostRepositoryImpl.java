package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserPostEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.UserPostMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserPostRepository;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserPostRepositoryImpl extends ServiceImpl<UserPostMapper, UserPostEntity> implements IUserPostRepository {

    /**
     * 通过用户id获取角色id列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link Long}>
     */
    @Override
    public List<Long> listPostByUserId(Long userId) {
        LambdaQueryWrapper<UserPostEntity> query = Wrappers.lambdaQuery();
        query.eq(UserPostEntity::getUserId, userId);
        List<UserPostEntity> list = baseMapper.selectList(query);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(UserPostEntity::getPostId).collect(Collectors.toList());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByUserId(Long userId, List<Long> postIds) {
        LambdaQueryWrapper<UserPostEntity> query = Wrappers.lambdaQuery();
        query.eq(UserPostEntity::getUserId, userId);
        baseMapper.delete(query);
        if (CollUtil.isEmpty(postIds)) {
            return;
        }

        List<UserPostEntity> entities = new ArrayList<>();
        for (Long postId : postIds) {
            UserPostEntity entity = new UserPostEntity();
            entity.setPostId(postId);
            entity.setUserId(userId);
            entities.add(entity);
        }
        boolean saveBatch = this.saveBatch(entities);
        if (!saveBatch) {
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}
