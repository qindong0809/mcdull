package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FolderEntity;

import java.util.List;

/**
 * folder repository
 *
 * @author dqcer
 * @since 2024/11/29
 */
public interface IFolderRepository extends IRepository<FolderEntity> {

    /**
     * insert
     *
     * @param entity entity
     * @return int
     */
    Integer insert(FolderEntity entity);

    /**
     * delete
     *
     * @param id id
     * @return int
     */
    boolean delete(Integer id, String reason);

    /**
     * list by parent id
     *
     * @param parentId parentId
     * @return int
     */
    List<FolderEntity> listByParentId(Integer parentId);

    /**
     * all
     *
     * @return int
     */
    List<FolderEntity> all();

    /**
     * get tree list
     *
     * @param folderId folderId
     * @return int
     */
    List<FolderEntity> getTreeList(Integer folderId);
}

