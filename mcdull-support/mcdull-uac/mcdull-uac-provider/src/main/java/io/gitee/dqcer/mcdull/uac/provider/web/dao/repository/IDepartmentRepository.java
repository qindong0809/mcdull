package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DepartmentEntity;

import java.util.List;

/**
 * Department repository
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IDepartmentRepository extends IService<DepartmentEntity> {

    /**
     * insert
     *
     * @param entity entity
     * @return int
     */
    Integer insert(DepartmentEntity entity);

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
    List<DepartmentEntity> listByParentId(Integer parentId);

    /**
     * all
     *
     * @return int
     */
    List<DepartmentEntity> all();

    /**
     * get tree list
     *
     * @param departmentId departmentId
     * @return int
     */
    List<DepartmentEntity> getTreeList(Integer departmentId);
}

