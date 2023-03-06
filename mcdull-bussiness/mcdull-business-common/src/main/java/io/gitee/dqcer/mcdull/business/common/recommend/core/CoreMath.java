package io.gitee.dqcer.mcdull.business.common.recommend.core;

import io.gitee.dqcer.mcdull.business.common.recommend.dto.RelateDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * 核心算法
 *
 * @author dqcer
 * @since 2023/03/06
 */
public class CoreMath {

    /**
     * 计算相关系数并排序
     *
     * @param key key
     * @param map map
     * @return Map<Integer, Double>
     */
    public static <S, T> Map<S, Double> computeNeighbor(S key, Map<S, List<RelateDTO<S, T>>>  map) {
        Map<S,Double> distMap = new TreeMap<>();
        List<RelateDTO<S, T>> userItems = map.get(key);
        if (Objects.isNull(userItems)) {
            return new HashMap<>();
        }
        map.forEach((k,v)->{
            //排除此用户
            if(!k.equals(key)){
                //关系系数
                double coefficient = relateDist(v,userItems,1);
                //关系距离
                double distance=Math.abs(coefficient);
                distMap.put(k,distance);
            }
        });
        return distMap;
    }

    public static <S, T> Map<T, Double> computeNeighborItem(T key, Map<T, List<RelateDTO<S, T>>>  map) {
        Map<T, Double> distMap = new TreeMap<>();
        List<RelateDTO<S, T>> userItems = map.get(key);
        map.forEach((k,v)->{
            //排除此用户
            if(!k.equals(key)){
                //关系系数
                double coefficient = relateDist(v,userItems,2);
                //关系距离
                double distance=Math.abs(coefficient);
                distMap.put(k,distance);
            }
        });
        return distMap;
    }


    /**
     * 计算两个序列间的相关系数
     *
     * @param xList xList
     * @param yList yList
     * @param type 类型0基于用户推荐 1基于物品推荐
     * @return double
     */
    private static <S, T> double relateDist(List<RelateDTO<S, T>> xList, List<RelateDTO<S, T>> yList, int type) {
        List<Integer> xs = new ArrayList<>();
        List<Integer> ys = new ArrayList<>();
        xList.forEach(x->{
            yList.forEach(y->{
                if(type==0){
                    if(x.getItemId().equals(y.getItemId())){
                        xs.add(x.getIndex());
                        ys.add(y.getIndex());
                    }
                }else{
                    if(x.getUseId().equals(y.getUseId())){
                        xs.add(x.getIndex());
                        ys.add(y.getIndex());
                    }
                }
            });
        });
        return getRelate(xs,ys);
    }

    /**
     * 方法描述: 皮尔森（pearson）相关系数计算
     *
     * @param xs x集合
     * @param ys y集合
     * @return {@link double}
     */
    public static double getRelate(List<Integer> xs, List<Integer> ys){
        int n = xs.size();
        //至少有两个元素
        final int minSize = 2;
        if (n < minSize) {
            return 0D;
        }
        double ex = xs.stream().mapToDouble(x -> x).sum();
        double ey = ys.stream().mapToDouble(y -> y).sum();
        double ex2 = xs.stream().mapToDouble(x -> Math.pow(x, 2)).sum();
        double ey2 = ys.stream().mapToDouble(y -> Math.pow(y, 2)).sum();
        double exy = IntStream.range(0, n).mapToDouble(i -> xs.get(i) * ys.get(i)).sum();
        double numerator = exy - ex * ey / n;
        double denominator = Math.sqrt((ex2 - Math.pow(ex, 2) / n) * (ey2 - Math.pow(ey, 2) / n));
        if (denominator == 0) {
            return 0D;
        }
        return numerator / denominator;
    }
}
