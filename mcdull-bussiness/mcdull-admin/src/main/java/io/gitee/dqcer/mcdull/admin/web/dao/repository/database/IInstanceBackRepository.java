package io.gitee.dqcer.mcdull.admin.web.dao.repository.database;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.entity.database.InstanceBackDO;

import java.util.List;

/**
* Instance back 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface IInstanceBackRepository extends IService<InstanceBackDO>  {
    List<InstanceBackDO> listByTicketId(Long ticketId);
}