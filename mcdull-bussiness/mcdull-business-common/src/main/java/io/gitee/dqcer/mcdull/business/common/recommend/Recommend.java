package io.gitee.dqcer.mcdull.business.common.recommend;



import io.gitee.dqcer.mcdull.business.common.recommend.dto.RelateDTO;

import java.util.List;

/**
 * 推荐算法入口
 *
 * @author dqcer
 * @date 2023/03/06
 */
public class Recommend {


    /**
     * 根据当前用户推荐: 猜你喜欢
     *
     * @param userId 用户id
     * @param data   数据
     * @return {@link List}<{@link T}>
     */
    public static <S, T> List<T> userCfRecommend(S userId, List<RelateDTO<S, T>> data) {
        return UserCF.recommend(userId, data);
    }

    /**
     * 根据物品推荐: 猜你喜欢
     *
     * @param itemId 物品id
     * @param data   数据
     * @return {@link List}<{@link T}>
     */
    public static <S, T> List<T> itemCfRecommend(T itemId, List<RelateDTO<S, T>> data) {
        return ItemCF.recommend(itemId, data);
    }

}
