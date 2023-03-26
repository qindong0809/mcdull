package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictDataDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictDataVO;

/**
 * 字典数据转换
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class DictDataConvert {

    public static DictDataVO convertToDictDataVO(DictDataDO dictDataDO) {
        DictDataVO dictDataVO = new DictDataVO();
        dictDataVO.setCreatedTime(dictDataDO.getCreatedTime());
        dictDataVO.setDictCode(dictDataDO.getId());
        dictDataVO.setDictSort(dictDataDO.getDictSort());
        dictDataVO.setDictLabel(dictDataDO.getDictLabel());
        dictDataVO.setDictValue(dictDataDO.getDictValue());
        dictDataVO.setDictType(dictDataDO.getDictType());
        dictDataVO.setCssClass(dictDataDO.getCssClass());
        dictDataVO.setListClass(dictDataDO.getListClass());
        dictDataVO.setIsDefault(dictDataDO.getIsDefault());
        dictDataVO.setStatus(String.valueOf(dictDataDO.getStatus()));
        dictDataVO.setRemark(dictDataDO.getRemark());
        return dictDataVO;
    }

}
