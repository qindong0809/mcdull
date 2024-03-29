package io.gitee.dqcer.mcdull.admin.web.dao.repository.database;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.InstanceEntity;

import java.util.List;

/**
* Instance 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface IInstanceRepository extends IService<InstanceEntity>  {

    Page<InstanceEntity> selectPage(InstanceListDTO dto);

    List<InstanceEntity> getListByName(String name);

    void removeUpdate(Long id);

    List<InstanceEntity> getByGroupId(Long groupId);
}