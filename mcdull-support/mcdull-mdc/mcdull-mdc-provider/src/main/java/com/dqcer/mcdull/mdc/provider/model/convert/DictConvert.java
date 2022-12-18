package com.dqcer.mcdull.mdc.provider.model.convert;

import com.dqcer.mcdull.mdc.provider.model.entity.DictDO;
import com.dqcer.mcdull.mdc.client.vo.DictClientVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DictConvert {


    /**
     * 实体转换来视图对象
     *
     * @param item 项
     * @return {@link DictClientVO}
     */
    public static DictClientVO entityConvertToVo(DictDO item) {
        if (item == null) {
            return null;
        }
        DictClientVO result = new DictClientVO();
        result.setCode(item.getCode());
        result.setParentCode(item.getParentCode());
        result.setName(item.getName());
        result.setNameShort(item.getNameShort());
        result.setSelectType(item.getSelectType());
        result.setSort(item.getSort());
        result.setDefaulted(item.getDefaulted());
        result.setStatus(item.getStatus());
        result.setRemark(item.getRemark());
        result.setLanguage(item.getLanguage());
        result.setDelFlag(item.getDelFlag());
        return result;
    }

    /**
     * 实体转换为列表
     *
     * @param item 项
     * @return {@link List}<{@link DictClientVO}>
     */
    public static List<DictClientVO> entitiesConvertToList(List<DictDO> item) {
        if (item == null) {
            return Collections.EMPTY_LIST;
        }
        List<DictClientVO> result = new ArrayList<>();
        for (DictDO entity : item) {
            result.add(entityConvertToVo(entity));
        }
        return result;
    }
}
