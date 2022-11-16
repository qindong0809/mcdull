package com.dqcer.mcdull.mdc.api.convert;

import com.dqcer.mcdull.mdc.api.entity.SysDictEntity;
import com.dqcer.mcdull.mdc.api.vo.SysDictVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SysDictConvert {


    /**
     * 实体转换来视图对象
     *
     * @param item 项
     * @return {@link SysDictVO}
     */
    public static SysDictVO entityConvertToVo(SysDictEntity item) {
        if (item == null) {
            return null;
        }
        SysDictVO result = new SysDictVO();
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
     * @return {@link List}<{@link SysDictVO}>
     */
    public static List<SysDictVO> entitiesConvertToList(List<SysDictEntity> item) {
        if (item == null) {
            return Collections.EMPTY_LIST;
        }
        List<SysDictVO> result = new ArrayList<>();
        for (SysDictEntity entity : item) {
            result.add(entityConvertToVo(entity));
        }
        return result;
    }
}
