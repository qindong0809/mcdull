package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FolderEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.FolderMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IFolderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹存储库
 *
 * @author dqcer
 * @since 2024/11/29
 */
@Service
public class FolderRepositoryImpl
        extends CrudRepository<FolderMapper, FolderEntity> implements IFolderRepository {

    @Override
    public Integer insert(FolderEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }


    @Override
    public boolean delete(Integer id, String reason) {
        return removeById(id);
    }

    @Override
    public List<FolderEntity> listByParentId(Integer parentId) {
        LambdaQueryWrapper<FolderEntity> query = Wrappers.lambdaQuery();
        query.eq(FolderEntity::getParentId, parentId);
        return baseMapper.selectList(query);
    }

    @Override
    public List<FolderEntity> all() {
        return baseMapper.selectList(null);
    }


    @Override
    public List<FolderEntity> getTreeList(Integer parentDeptId) {
        return this.getChildNodeByParentId(parentDeptId);
    }

    @Override
    public FolderEntity getSystemFolderId(Integer parentId, String name) {
        LambdaQueryWrapper<FolderEntity> query = Wrappers.lambdaQuery();
        query.eq(FolderEntity::getParentId, parentId);
        query.eq(FolderEntity::getName, name);
        List<FolderEntity> list = super.list(query);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    private List<FolderEntity> getChildNodeByParentId(Integer parentId) {
        List<FolderEntity> result = new ArrayList<>();
        List<FolderEntity> list = this.listByParentId(parentId);
        if (CollUtil.isNotEmpty(list)) {
            result.addAll(list);
            for (FolderEntity entity : list) {
                List<FolderEntity> childNodeList = getChildNodeByParentId(entity.getId());
                if (CollUtil.isNotEmpty(childNodeList)) {
                    result.addAll(childNodeList);
                }
            }
        }
        return result;
    }
}
