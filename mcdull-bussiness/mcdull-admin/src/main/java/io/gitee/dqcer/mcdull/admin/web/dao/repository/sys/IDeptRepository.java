package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DeptEntity;

import java.util.List;

/**
* 部门 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface IDeptRepository extends IService<DeptEntity>  {

    List<DeptEntity> list(String name, String status);
}