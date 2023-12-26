package io.gitee.dqcer.mcdull.business.common.engine;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 对象领域常用方法封装
 *
 * @author dqcer
 * @since 2023/06/14
 */
@SuppressWarnings("unused")
public class DomainEngine {

    /**
     * 比较器（必须要有id字段）
     * 业务场景：用于一个表单提交包含多个子表属性的数据结构，返回哪些是新增、修改、删除
     *
     * @param dbQueryEntityList              数据库查询实体列表
     * @param frontEndParamConvertEntityList 前端参数转换实体列表
     * @return {@link CompareBean}<{@link T}>
     */
    public static <T extends Serializable> CompareBean<T> compare(List<T> dbQueryEntityList, List<T> frontEndParamConvertEntityList) {
        CompareBean<T> bean = new CompareBean<>();
        if (CollUtil.isEmpty(frontEndParamConvertEntityList)) {
            bean.setRemoveList(dbQueryEntityList);
            return bean;
        }
        if (CollUtil.isEmpty(dbQueryEntityList)) {
            bean.setInsertList(frontEndParamConvertEntityList);
            return bean;
        }

        Map<Integer, T> oldMap = getMap(dbQueryEntityList);
        List<Integer> oldIdList = new ArrayList<>();
        for (Map.Entry<Integer, T> entry : oldMap.entrySet()) {
            oldIdList.add(entry.getKey());
        }

        Map<Integer, T> newMap = getMap(frontEndParamConvertEntityList);
        List<Integer> newIdList = new ArrayList<>();
        for (Map.Entry<Integer, T> entry : newMap.entrySet()) {
            newIdList.add(entry.getKey());
        }

        Set<Integer> allList = new HashSet<>();
        allList.addAll(oldIdList);
        allList.addAll(newIdList);

        List<Integer> removeIdList = CollUtil.subtractToList(allList, newIdList);

        List<T> removeList = new ArrayList<>();
        for (Integer integer : removeIdList) {
            removeList.add(oldMap.get(integer));
        }
        bean.setRemoveList(removeList);

        List<T> insertList = new ArrayList<>();
        List<T> updateList = new ArrayList<>();
        for (T t : frontEndParamConvertEntityList) {
            Object invoke = ReflectUtil.invoke(t, "getId");
            if (invoke != null) {
                updateList.add(t);
            } else {
                insertList.add(t);
            }
        }
        bean.setInsertList(insertList);
        bean.setUpdateList(updateList);
        return bean;
    }

    private static <T> Map<Integer, T> getMap(List<T> list) {
        Map<Integer, T> map = new HashMap<>(16);
        for (T t : list) {
            Object invoke = ReflectUtil.invoke(t, "getId");
            if (invoke != null) {
                map.put((Integer) invoke, t);
            }
        }
        return map;
    }


    public static class CompareBean<T extends Serializable> {

        private List<T> insertList;

        private List<T> updateList;

        private List<T> removeList;

        public List<T> getInsertList() {
            return insertList;
        }

        public void setInsertList(List<T> insertList) {
            this.insertList = insertList;
        }

        public List<T> getUpdateList() {
            return updateList;
        }

        public void setUpdateList(List<T> updateList) {
            this.updateList = updateList;
        }

        public List<T> getRemoveList() {
            return removeList;
        }

        public void setRemoveList(List<T> removeList) {
            this.removeList = removeList;
        }
    }


}
