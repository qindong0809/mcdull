package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictTypeEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictTypeVO;

/**
* 通知公告表 对象转换工具类
*
* @author dqcer
* @since 2023-01-18
*/
public class DictTypeConvert {

    public static DictTypeVO convertToDictTypeVO(DictTypeEntity item){
        DictTypeVO dictTypeVO = new DictTypeVO();
        dictTypeVO.setRemark(item.getRemark());
        dictTypeVO.setCreatedTime(item.getCreatedTime());
        dictTypeVO.setDictId(item.getId());
        dictTypeVO.setDictName(item.getDictName());
        dictTypeVO.setDictType(item.getDictType());
        dictTypeVO.setStatus(item.getStatus());
        return dictTypeVO;
    }

    public static DictTypeEntity convertToDictTypeDo(DictTypeAddDTO dto) {
        DictTypeEntity dictTypeDO = new DictTypeEntity();
        dictTypeDO.setDictName(dto.getDictName());
        dictTypeDO.setDictType(dto.getDictType());
        dictTypeDO.setStatus(dto.getStatus());
        dictTypeDO.setRemark(dto.getRemark());
        return dictTypeDO;
    }
}