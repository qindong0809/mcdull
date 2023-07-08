package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeAddDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictTypeDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictTypeVO;

/**
* 通知公告表 对象转换工具类
*
* @author dqcer
* @since 2023-01-18
*/
public class DictTypeConvert {

    public static DictTypeVO convertToDictTypeVO(DictTypeDO item){
        DictTypeVO dictTypeVO = new DictTypeVO();
        dictTypeVO.setRemark(item.getRemark());
        dictTypeVO.setCreatedTime(item.getCreatedTime());
        dictTypeVO.setDictId(item.getId());
        dictTypeVO.setDictName(item.getDictName());
        dictTypeVO.setDictType(item.getDictType());
        dictTypeVO.setStatus(item.getStatus());
        return dictTypeVO;
    }

    public static DictTypeDO convertToDictTypeDo(DictTypeAddDTO dto) {
        DictTypeDO dictTypeDO = new DictTypeDO();
        dictTypeDO.setDictName(dto.getDictName());
        dictTypeDO.setDictType(dto.getDictType());
        dictTypeDO.setStatus(dto.getStatus());
        dictTypeDO.setRemark(dto.getRemark());
        return dictTypeDO;
    }
}