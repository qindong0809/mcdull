package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.SerialNumberRecordQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberRecordEntity;

import java.util.List;

/**
 * Serial number record service
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ISerialNumberRecordService extends IRepository<SerialNumberRecordEntity> {

    /**
     * query
     *
     * @param queryForm queryForm
     * @return {@link PagedVO }<{@link SerialNumberRecordEntity }>
     */
    PagedVO<SerialNumberRecordEntity> query(SerialNumberRecordQueryDTO queryForm);

    /**
     * list
     *
     * @param serialNumberId serialNumberId
     * @return {@link List }<{@link SerialNumberRecordEntity }>
     */
    List<SerialNumberRecordEntity> getListBySerialNumber(Integer serialNumberId);

    /**
     * save
     *
     * @param oldRecord oldRecord
     * @param resultList resultList
     */
    void batchSave(SerialNumberRecordEntity oldRecord, List<Integer> resultList);

    /**
     * save
     *
     * @param entity     entity
     * @param resultList resultList
     */
    void batchSave(SerialNumberEntity entity, List<Integer> resultList);

}
