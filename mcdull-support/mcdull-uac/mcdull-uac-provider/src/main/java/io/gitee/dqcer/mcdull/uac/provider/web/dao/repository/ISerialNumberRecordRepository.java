package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SerialNumberRecordEntity;

import java.util.List;

/**
* 数据库操作封装接口层
*
* @author dqcer
* @since 2024-04-29
*/
public interface ISerialNumberRecordRepository extends IService<SerialNumberRecordEntity>  {


    List<SerialNumberRecordEntity> queryListByIds(List<Integer> idList);

    Page<SerialNumberRecordEntity> selectPage(ChangeLogQueryDTO param);

    SerialNumberRecordEntity getById(Integer id);

    void insert(SerialNumberRecordEntity entity);

    void deleteBatchByIds(List<Integer> ids);

    boolean exist(SerialNumberRecordEntity entity);
}