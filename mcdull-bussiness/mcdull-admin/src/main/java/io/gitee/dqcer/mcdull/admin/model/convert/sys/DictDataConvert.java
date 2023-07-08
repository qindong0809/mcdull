package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataAddDTO;
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
        dictDataVO.setStatus(dictDataDO.getStatus());
        dictDataVO.setRemark(dictDataDO.getRemark());
        return dictDataVO;
    }

    public static DictDataDO convertToDictDataDo(DictDataAddDTO dto) {
        DictDataDO dictDataDO = new DictDataDO();
        dictDataDO.setDictSort(dto.getDictSort());
        dictDataDO.setDictLabel(dto.getDictLabel());
        dictDataDO.setDictValue(dto.getDictValue());
        dictDataDO.setDictType(dto.getDictType());
        dictDataDO.setCssClass(dto.getCssClass());
        dictDataDO.setListClass(dto.getListClass());
        dictDataDO.setIsDefault("N");
        dictDataDO.setStatus(dto.getStatus());
        dictDataDO.setRemark(dto.getRemark());
        return dictDataDO;
    }
}
