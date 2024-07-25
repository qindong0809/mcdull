package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SerialNumberEntity;

import java.util.List;

/**
 * Serial Number Repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ISerialNumberRepository extends IService<SerialNumberEntity>  {

    /**
     * query list by ids
     *
     * @param idList id list
     * @return {@link List}<{@link SerialNumberEntity}>
     */
    List<SerialNumberEntity> queryListByIds(List<Integer> idList);

    /**
     * select page
     *
     * @param param param
     * @return {@link Page}<{@link SerialNumberEntity}>
     */
    Page<SerialNumberEntity> selectPage(ChangeLogQueryDTO param);

    /**
     * get by id
     *
     * @param id id
     * @return {@link SerialNumberEntity}
     */
    SerialNumberEntity getById(Integer id);

    /**
     * insert
     *
     * @param entity entity
     */
    void insert(SerialNumberEntity entity);

    /**
     * delete batch by ids
     *
     * @param ids ids
     */
    void deleteBatchByIds(List<Integer> ids);

    /**
     * exist
     *
     * @param entity entity
     * @return boolean
     */
    boolean exist(SerialNumberEntity entity);
}