package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.PostLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.PostEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.PostMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IPostRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* 岗位信息 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class PostRepositoryImpl extends ServiceImpl<PostMapper, PostEntity>  implements IPostRepository {

    @Override
    public Page<PostEntity> selectPage(PostLiteDTO dto) {
        LambdaQueryWrapper<PostEntity> query = Wrappers.lambdaQuery();
        query.orderByDesc(BaseEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), query);
    }

    @Override
    public boolean checkBusinessUnique(Long id, String postName) {
        LambdaQueryWrapper<PostEntity> query = Wrappers.lambdaQuery();
        if (ObjUtil.isNotNull(id)) {
            query.ne(IdEntity::getId, id);
        }
        query.eq(PostEntity::getPostName, postName);
        return !baseMapper.exists(query);
    }

    @Override
    public List<PostEntity> getAll() {
        LambdaQueryWrapper<PostEntity> query = Wrappers.lambdaQuery();
        List<PostEntity> list = baseMapper.selectList(query);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }
}