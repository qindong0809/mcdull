package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SerialNumberEntity;

import java.util.List;

/**
* 数据库操作封装接口层
*
* @author dqcer
* @since 2024-04-29
*/
public interface ISerialNumberRepository extends IService<SerialNumberEntity>  {


    List<SerialNumberEntity> queryListByIds(List<Integer> idList);

    Page<SerialNumberEntity> selectPage(ChangeLogQueryDTO param);

    SerialNumberEntity getById(Integer id);

    void insert(SerialNumberEntity entity);

    void deleteBatchByIds(List<Integer> ids);

    boolean exist(SerialNumberEntity entity);
}