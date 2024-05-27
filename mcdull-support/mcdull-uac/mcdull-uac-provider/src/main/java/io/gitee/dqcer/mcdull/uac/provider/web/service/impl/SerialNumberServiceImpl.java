package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SerialNumberEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.SerialNumberBusinessTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.SerialNumberVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ISerialNumberRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ISerialNumberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class SerialNumberServiceImpl
        extends BasicServiceImpl<ISerialNumberRepository> implements ISerialNumberService {


    @Override
    public List<SerialNumberVO> getAll() {
        List<SerialNumberVO> voList = new ArrayList<>();
        List<SerialNumberEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            for (SerialNumberEntity entity : list) {
                SerialNumberVO vo = this.convertToVO(entity);
                Integer businessType = entity.getBusinessType();
                IEnum<Integer> typeEnum = IEnum.getByCode(SerialNumberBusinessTypeEnum.class, businessType);
                if (ObjUtil.isNotEmpty(typeEnum)) {
                    vo.setBusinessName(typeEnum.getText());
                }
                voList.add(vo);
            }
        }
        return voList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> generate(SerialNumberGenerateDTO dto) {
        SerialNumberEntity entity = (SerialNumberEntity) super.checkDataExistById(dto.getSerialNumberId());
        String format = entity.getFormat();
        return null;
    }

    private SerialNumberVO convertToVO(SerialNumberEntity entity) {
        SerialNumberVO serialNumberVO = new SerialNumberVO();
        serialNumberVO.setSerialNumberId(entity.getId());
        serialNumberVO.setBusinessType(entity.getBusinessType());
        serialNumberVO.setFormat(entity.getFormat());
        serialNumberVO.setRuleType(entity.getRuleType());
        serialNumberVO.setInitNumber(entity.getInitNumber());
        serialNumberVO.setStepRandomRange(entity.getStepRandomRange());
        serialNumberVO.setRemark(entity.getRemark());
        serialNumberVO.setLastNumber(entity.getLastNumber());
        serialNumberVO.setLastTime(entity.getLastTime());
        return serialNumberVO;
    }
}
