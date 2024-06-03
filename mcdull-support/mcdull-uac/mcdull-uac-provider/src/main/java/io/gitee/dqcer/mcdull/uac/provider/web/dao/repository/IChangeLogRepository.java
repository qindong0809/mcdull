package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ChangeLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ChangeLogEntity;

import java.util.List;

/**
* 数据库操作封装接口层
*
* @author dqcer
* @since 2024-04-29
*/
public interface IChangeLogRepository extends IService<ChangeLogEntity>  {


    List<ChangeLogEntity> queryListByIds(List<Integer> idList);

    Page<ChangeLogEntity> selectPage(ChangeLogQueryDTO param);

    ChangeLogEntity getById(Integer id);

    void insert(ChangeLogEntity entity);

    void deleteBatchByIds(List<Integer> ids);

    boolean exist(ChangeLogEntity entity);
}