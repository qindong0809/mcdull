package io.gitee.dqcer.mcdull.system.provider.web.service;

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
public interface ISerialNumberRecordService {

    PagedVO<SerialNumberRecordEntity> query(SerialNumberRecordQueryDTO queryForm);

    List<SerialNumberRecordEntity> getListBySerialNumber(Integer serialNumberId);

    void batchSave(SerialNumberRecordEntity oldRecord, List<Integer> resultList);

    void batchSave(SerialNumberEntity entity, List<Integer> resultList);

}
