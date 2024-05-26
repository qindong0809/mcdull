package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.OperateLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OperateLogEntity;

import java.util.List;
import java.util.Map;

/**
* @author dqcer
* @since 2024-04-29
*/
public interface IOperateLogRepository extends IService<OperateLogEntity>  {


   /**
    * 按条件分页查询
    *
    * @param param      参数
    * @param userIdList
    * @return {@link Page< OperateLogEntity >}
    */
    Page<OperateLogEntity> selectPage(OperateLogQueryDTO param, List<Integer> userIdList);

    List<Map<String, Object>> home();

}