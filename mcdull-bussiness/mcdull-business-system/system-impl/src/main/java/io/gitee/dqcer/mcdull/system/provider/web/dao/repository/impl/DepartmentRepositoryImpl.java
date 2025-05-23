package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.DepartmentMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IDepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色菜单 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2023/12/01
 */
@Service
public class DepartmentRepositoryImpl
        extends CrudRepository<DepartmentMapper, DepartmentEntity> implements IDepartmentRepository {

    @Override
    public Integer insert(DepartmentEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }


    @Override
    public boolean delete(Integer id, String reason) {
        return removeById(id);
    }

    @Override
    public List<DepartmentEntity> listByParentId(Integer parentId) {
        LambdaQueryWrapper<DepartmentEntity> query = Wrappers.lambdaQuery();
        query.eq(DepartmentEntity::getParentId, parentId);
        return baseMapper.selectList(query);
    }

    @Override
    public List<DepartmentEntity> all() {
        return baseMapper.selectList(null);
    }


    @Override
    public List<DepartmentEntity> getTreeList(Integer parentDeptId) {
        return this.getChildNodeByParentId(parentDeptId);
    }

    private List<DepartmentEntity> getChildNodeByParentId(Integer parentId) {
        List<DepartmentEntity> result = new ArrayList<>();
        List<DepartmentEntity> list = this.listByParentId(parentId);
        if (CollUtil.isNotEmpty(list)) {
            result.addAll(list);
            for (DepartmentEntity entity : list) {
                List<DepartmentEntity> childNodeList = getChildNodeByParentId(entity.getId());
                if (CollUtil.isNotEmpty(childNodeList)) {
                    result.addAll(childNodeList);
                }
            }
        }
        return result;
    }
}
