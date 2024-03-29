package io.gitee.dqcer.mcdull.admin.web.dao.repository.database;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GroupEntity;

import java.util.List;

/**
* Instance 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface IGroupRepository extends IService<GroupEntity>  {

    Page<GroupEntity> selectPage(GroupListDTO dto);

    List<GroupEntity> getListByName(String name);

    void removeUpdate(Long id);

    List<GroupEntity> allList();

}