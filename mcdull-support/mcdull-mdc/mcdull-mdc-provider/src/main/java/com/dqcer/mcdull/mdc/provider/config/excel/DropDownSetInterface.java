package com.dqcer.mcdull.mdc.provider.config.excel;

/**
 * 下拉选项接口
 *
 * @author dqcer
 * @date 2022/11/20 22:11:90
 */
public interface DropDownSetInterface {

    /**
     * 数据集
     *
     * @param typeEnum
     * @param userId
     * @param roleId
     * @param studyId
     * @return
     */
    String[] getSource(SelectTypeEnum typeEnum, Long userId, Long roleId, Long studyId);
 
}
 
