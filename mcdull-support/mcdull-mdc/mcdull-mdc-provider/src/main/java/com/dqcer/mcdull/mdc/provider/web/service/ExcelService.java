package com.dqcer.mcdull.mdc.provider.web.service;

import com.dqcer.mcdull.mdc.provider.config.excel.DropDownSetField;
import com.dqcer.mcdull.mdc.provider.config.excel.DropDownSetInterface;
import com.dqcer.mcdull.mdc.provider.config.excel.SelectTypeEnum;
import org.springframework.stereotype.Service;

@Service
public class ExcelService implements DropDownSetInterface {
    /**
     * 数据集
     *
     * @param typeEnum
     * @param userId
     * @param roleId
     * @param studyId
     * @return
     */
    @Override
    public String[] getSource(SelectTypeEnum typeEnum, Long userId, Long roleId, Long studyId) {
        return new String[0];
    }
}