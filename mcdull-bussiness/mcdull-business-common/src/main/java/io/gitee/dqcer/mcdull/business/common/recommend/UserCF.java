package io.gitee.dqcer.mcdull.business.common.recommend;



import io.gitee.dqcer.mcdull.business.common.recommend.core.CoreMath;
import io.gitee.dqcer.mcdull.business.common.recommend.dto.RelateDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基于用户的协同过滤算法(user-based collaboratIve filtering 简称 UserCF)
 *
 * 给用户推荐和他兴趣相似的其他用户喜欢的产品
 *
 * @author dqcer
 * @since 2023/02/24
 */
public class UserCF {
    /**
     * 方法描述: 推荐电影id列表
     *
     * @param userId 当前用户
     * @param list 用户电影评分数据
     * @return {@link List <Integer>}
     * @date 2023年02月02日 14:51:42
     */
    public static <S, T> List<T> recommend(S userId, List<RelateDTO<S, T>> list) {
        //按用户分组
        Map<S, List<RelateDTO<S, T>>> userMap = list.stream().collect(Collectors.groupingBy(RelateDTO::getUseId));
        //获取其他用户与当前用户的关系值
        Map<S,Double>  userDisMap = CoreMath.computeNeighbor(userId, userMap);
        if (userDisMap.isEmpty()) {
            return Collections.emptyList();
        }
        //获取关系最近的用户
        double maxValue = Collections.max(userDisMap.values());
        Set<S> userIds = userDisMap.entrySet().stream().filter(e -> e.getValue() == maxValue).map(Map.Entry::getKey).collect(Collectors.toSet());
        //取关系最近的用户
        S nearestUserId = userIds.stream().findAny().orElse(null);
        if (nearestUserId == null) {
            return Collections.emptyList();
        }
        //最近邻用户看过电影列表
        List<T> neighborItems = userMap.get(nearestUserId).stream().map(RelateDTO::getItemId).collect(Collectors.toList());
        //指定用户看过电影列表
        List<T> userItems = userMap.get(userId).stream().map(RelateDTO::getItemId).collect(Collectors.toList());
        //找到最近邻看过，但是该用户没看过的电影
        neighborItems.removeAll(userItems);
        return neighborItems;
    }
}
