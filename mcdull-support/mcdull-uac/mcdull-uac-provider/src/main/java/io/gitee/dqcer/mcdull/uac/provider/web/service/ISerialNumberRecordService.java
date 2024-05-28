package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SerialNumberRecordQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SerialNumberRecordEntity;

import java.util.List;

/**
* @author dqcer
* @since 2024-04-29
*/
public interface ISerialNumberRecordService {

    PagedVO<SerialNumberRecordEntity> query(SerialNumberRecordQueryDTO queryForm);

    List<SerialNumberRecordEntity> getListBySerialNumber(Integer serialNumberId);

    void batchSave(SerialNumberRecordEntity oldRecord, List<Integer> resultList);
}
