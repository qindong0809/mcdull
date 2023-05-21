package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserPostDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.UserPostMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserPostRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.util.ObjUtil;
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
public class UserPostRepositoryImpl extends ServiceImpl<UserPostMapper, UserPostDO> implements IUserPostRepository {

    /**
     * 通过用户id获取角色id列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link Long}>
     */
    @Override
    public List<Long> listPostByUserId(Long userId) {
        LambdaQueryWrapper<UserPostDO> query = Wrappers.lambdaQuery();
        query.eq(UserPostDO::getUserId, userId);
        List<UserPostDO> list = baseMapper.selectList(query);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(UserPostDO::getPostId).collect(Collectors.toList());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByUserId(Long userId, List<Long> postIds) {
        LambdaQueryWrapper<UserPostDO> query = Wrappers.lambdaQuery();
        query.eq(UserPostDO::getUserId, userId);
        int row = baseMapper.delete(query);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }

        if (ObjUtil.isNull(postIds)) {
            return;
        }

        List<UserPostDO> entities = new ArrayList<>();
        for (Long postId : postIds) {
            UserPostDO entity = new UserPostDO();
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
