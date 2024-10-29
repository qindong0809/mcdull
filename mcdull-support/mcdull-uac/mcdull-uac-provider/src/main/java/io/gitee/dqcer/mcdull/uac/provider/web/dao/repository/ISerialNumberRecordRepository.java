package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SerialNumberRecordQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SerialNumberRecordEntity;

import java.util.List;

/**
 * Serial Number Record Repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ISerialNumberRecordRepository extends IRepository<SerialNumberRecordEntity> {

    /**
     * 查询列表
     *
     * @param idList idList
     * @return {@link List}<{@link SerialNumberRecordEntity}>
     */
    List<SerialNumberRecordEntity> queryListByIds(List<Integer> idList);

    /**
     * 分页
     *
     * @param param param
     * @return {@link Page}<{@link SerialNumberRecordEntity}>
     */
    Page<SerialNumberRecordEntity> selectPage(SerialNumberRecordQueryDTO param);

    /**
     * 获取
     *
     * @param id id
     * @return {@link SerialNumberRecordEntity}
     */
    SerialNumberRecordEntity getById(Integer id);

    /**
     * 插入
     *
     * @param entity entity
     */
    void insert(SerialNumberRecordEntity entity);

    /**
     * 删除
     *
     * @param ids ids
     */
    void deleteBatchByIds(List<Integer> ids);

    /**
     * 存在
     *
     * @param entity entity
     * @return boolean
     */
    boolean exist(SerialNumberRecordEntity entity);

    /**
     * 获取列表
     *
     * @param serialNumberId serialNumberId
     * @return {@link List}<{@link SerialNumberRecordEntity}>
     */
    List<SerialNumberRecordEntity> getListBySerialNumber(Integer serialNumberId);
}