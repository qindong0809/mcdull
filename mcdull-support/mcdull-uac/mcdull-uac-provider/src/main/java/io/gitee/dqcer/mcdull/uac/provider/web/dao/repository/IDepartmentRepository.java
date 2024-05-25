package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DepartmentEntity;

import java.util.List;

/**
 * @author dqcer
 * @since 2022/12/26
 */
public interface IDepartmentRepository extends IService<DepartmentEntity> {


    Integer insert(DepartmentEntity entity);

    boolean delete(Integer id, String reason);

    List<DepartmentEntity> listByParentId(Integer parentId);

    List<DepartmentEntity> all();

    List<DepartmentEntity> getTreeList(Integer departmentId);
}

