package io.gitee.dqcer.mcdull.admin.web.dao.repository.database;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.entity.database.BackInstanceEntity;

import java.util.List;

/**
* Instance back 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface IBackInstanceRepository extends IService<BackInstanceEntity>  {
    List<BackInstanceEntity> listByBackId(Long backId);

}