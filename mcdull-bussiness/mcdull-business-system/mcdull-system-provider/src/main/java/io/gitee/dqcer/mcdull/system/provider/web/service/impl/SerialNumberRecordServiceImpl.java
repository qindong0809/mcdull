package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.SerialNumberRecordQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SerialNumberRecordEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.ISerialNumberRecordRepository;
import io.gitee.dqcer.mcdull.system.provider.web.service.ISerialNumberRecordService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Serial Number Record Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class SerialNumberRecordServiceImpl
        extends BasicServiceImpl<ISerialNumberRecordRepository> implements ISerialNumberRecordService {


    @Override
    public PagedVO<SerialNumberRecordEntity> query(SerialNumberRecordQueryDTO dto) {
        Page<SerialNumberRecordEntity> entityPage = baseRepository.selectPage(dto);
        return PageUtil.toPage(entityPage);
    }

    @Override
    public List<SerialNumberRecordEntity> getListBySerialNumber(Integer serialNumberId) {
        return baseRepository.getListBySerialNumber(serialNumberId);
    }

    @Override
    public void batchSave(SerialNumberRecordEntity oldRecord, List<Integer> resultList) {
        List<SerialNumberRecordEntity> list = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            SerialNumberRecordEntity entity = new SerialNumberRecordEntity();
            entity.setSerialNumberId(oldRecord.getSerialNumberId());
            entity.setLastNumber(resultList.get(i));
            entity.setLastTime(new Date());
            entity.setRecordDate(new Date());
            entity.setCount(oldRecord.getCount() + i + 1);
            list.add(entity);
        }
        baseRepository.saveBatch(list, list.size());
    }

    @Override
    public void batchSave(SerialNumberEntity configEntity, List<Integer> resultList) {
        List<SerialNumberRecordEntity> list = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            SerialNumberRecordEntity entity = new SerialNumberRecordEntity();
            entity.setSerialNumberId(configEntity.getId());
            entity.setLastNumber(resultList.get(i));
            entity.setLastTime(new Date());
            entity.setRecordDate(new Date());
            entity.setCount(i + 1);
            list.add(entity);
        }
        baseRepository.saveBatch(list, list.size());
    }
}
