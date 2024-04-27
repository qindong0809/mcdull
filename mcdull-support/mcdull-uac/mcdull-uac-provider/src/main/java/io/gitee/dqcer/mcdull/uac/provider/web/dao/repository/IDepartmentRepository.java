package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DepartmentEntity;

import java.util.List;

/**
 * @author dqcer
 * @since 2022/12/26
 */
public interface IDepartmentRepository extends IService<DepartmentEntity> {


    Long insert(DepartmentEntity entity);

    boolean delete(Long id, String reason);

    List<DepartmentEntity> listByParentId(Long parentId);

    List<DepartmentEntity> all();
}

