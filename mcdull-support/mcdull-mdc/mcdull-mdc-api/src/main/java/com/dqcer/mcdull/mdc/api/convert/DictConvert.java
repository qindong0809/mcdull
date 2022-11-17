package com.dqcer.mcdull.mdc.api.convert;

import com.dqcer.mcdull.mdc.api.entity.SysDictEntity;
import com.dqcer.mcdull.mdc.api.vo.DictVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DictConvert {


    /**
     * 实体转换来视图对象
     *
     * @param item 项
     * @return {@link DictVO}
     */
    public static DictVO entityConvertToVo(SysDictEntity item) {
        if (item == null) {
            return null;
        }
        DictVO result = new DictVO();
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
     * @return {@link List}<{@link DictVO}>
     */
    public static List<DictVO> entitiesConvertToList(List<SysDictEntity> item) {
        if (item == null) {
            return Collections.EMPTY_LIST;
        }
        List<DictVO> result = new ArrayList<>();
        for (SysDictEntity entity : item) {
            result.add(entityConvertToVo(entity));
        }
        return result;
    }
}
