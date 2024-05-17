package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OperateLogEntity;

/**
* @author dqcer
* @since 2024-04-29
*/
public interface IOperateLogRepository extends IService<OperateLogEntity>  {


   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page< OperateLogEntity >}
    */
    Page<OperateLogEntity> selectPage(ChangeLogQueryDTO param);

}