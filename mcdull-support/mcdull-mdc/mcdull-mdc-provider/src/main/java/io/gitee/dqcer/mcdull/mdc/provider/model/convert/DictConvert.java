package io.gitee.dqcer.mcdull.mdc.provider.model.convert;

import io.gitee.dqcer.mcdull.mdc.client.vo.DictTypeClientVO;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictTypeEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 码表转换
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class DictConvert {


    /**
     * 实体转换来视图对象
     *
     * @param item 项
     * @return {@link DictTypeClientVO}
     */
    public static DictTypeClientVO entityConvertToVo(DictTypeEntity item) {
        DictTypeClientVO dictTypeClientVO = new DictTypeClientVO();
        dictTypeClientVO.setId(item.getId());
        dictTypeClientVO.setDictName(item.getDictName());
        dictTypeClientVO.setDictType(item.getDictType());
        dictTypeClientVO.setRemark(item.getRemark());
        dictTypeClientVO.setInactive(item.getInactive());
        return dictTypeClientVO;

    }

    /**
     * 实体转换为列表
     *
     * @param item 项
     * @return {@link List}<{@link DictTypeClientVO}>
     */
    public static List<DictTypeClientVO> entitiesConvertToList(List<DictTypeEntity> item) {
        if (item == null) {
            return Collections.EMPTY_LIST;
        }
        List<DictTypeClientVO> result = new ArrayList<>();
        for (DictTypeEntity entity : item) {
            result.add(entityConvertToVo(entity));
        }
        return result;
    }
}
