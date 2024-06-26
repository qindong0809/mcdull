package io.gitee.dqcer.mcdull.admin.web.dao.repository.database;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GitListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GitEntity;

import java.util.List;

/**
* Instance 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface IGitRepository extends IService<GitEntity>  {

    Page<GitEntity> selectPage(GitListDTO dto);

    List<GitEntity> getListByName(String name);

    void removeUpdate(Long id);

    List<GitEntity> allList();

}