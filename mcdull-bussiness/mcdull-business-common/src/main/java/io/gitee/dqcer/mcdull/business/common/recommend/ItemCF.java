package io.gitee.dqcer.mcdull.business.common.recommend;



import io.gitee.dqcer.mcdull.business.common.recommend.core.CoreMath;
import io.gitee.dqcer.mcdull.business.common.recommend.dto.RelateDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于物品的协同过滤算法
 *
 * @author dqcer
 * @since 2023/03/06
 */
public class ItemCF {

    public static <S, T> List<T> recommend(T itemId, List<RelateDTO<S, T>> list) {
        //按物品分组
        Map<T, List<RelateDTO<S, T>>> itemMap=list.stream().collect(Collectors.groupingBy(RelateDTO::getItemId));
        //获取其他物品与当前物品的关系值
        Map<T,Double>  itemDisMap = CoreMath.computeNeighborItem(itemId, itemMap);
        //获取关系最近物品
        double maxValue= Collections.max(itemDisMap.values());
        return itemDisMap.entrySet().stream().filter(e->e.getValue()==maxValue).map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
