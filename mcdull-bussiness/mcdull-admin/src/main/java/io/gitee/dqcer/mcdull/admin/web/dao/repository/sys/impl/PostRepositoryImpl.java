package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.PostLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.PostDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.PostMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IPostRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
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
public class PostRepositoryImpl extends ServiceImpl<PostMapper, PostDO>  implements IPostRepository {

    @Override
    public Page<PostDO> selectPage(PostLiteDTO dto) {
        LambdaQueryWrapper<PostDO> query = Wrappers.lambdaQuery();
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        query.orderByDesc(BaseDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    @Override
    public boolean checkBusinessUnique(Long id, String postName) {
        LambdaQueryWrapper<PostDO> query = Wrappers.lambdaQuery();
        if (ObjUtil.isNotNull(id)) {
            query.ne(IdDO::getId, id);
        }
        query.eq(PostDO::getPostName, postName);
        return !baseMapper.exists(query);
    }

    @Override
    public List<PostDO> getAll() {
        LambdaQueryWrapper<PostDO> query = Wrappers.lambdaQuery();
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        List<PostDO> list = baseMapper.selectList(query);
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }
}