package io.gitee.dqcer.mcdull.framework.base.engine;

import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;

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
    public static <T extends IdDO> CompareBean<T> compare(List<T> dbQueryEntityList,
                                                          List<T> frontEndParamConvertEntityList) {
        CompareBean<T> bean = new CompareBean<>();
        if (CollUtil.isEmpty(frontEndParamConvertEntityList)) {
            bean.setRemoveList(dbQueryEntityList);
            return bean;
        }
        if (CollUtil.isEmpty(dbQueryEntityList)) {
            bean.setInsertList(frontEndParamConvertEntityList);
            return bean;
        }

        Map<Long, T> oldMap = getMap(dbQueryEntityList);
        List<Long> oldIdList = new ArrayList<>();
        for (Map.Entry<Long, T> entry : oldMap.entrySet()) {
            oldIdList.add(entry.getKey());
        }

        Map<Long, T> newMap = getMap(frontEndParamConvertEntityList);
        List<Long> newIdList = new ArrayList<>();
        for (Map.Entry<Long, T> entry : newMap.entrySet()) {
            newIdList.add(entry.getKey());
        }

        Set<Long> allList = new HashSet<>();
        allList.addAll(oldIdList);
        allList.addAll(newIdList);

        List<Long> removeIdList = CollUtil.subtractToList(allList, newIdList);

        List<T> removeList = new ArrayList<>();
        for (Long id : removeIdList) {
            removeList.add(oldMap.get(id));
        }
        bean.setRemoveList(removeList);

        List<T> insertList = new ArrayList<>();
        List<T> updateList = new ArrayList<>();
        for (T t : frontEndParamConvertEntityList) {
            Long id = t.getId();
            if (id != null) {
                updateList.add(t);
            } else {
                insertList.add(t);
            }
        }
        bean.setInsertList(insertList);
        bean.setUpdateList(updateList);
        return bean;
    }

    private static <T extends IdDO> Map<Long, T> getMap(List<T> list) {
        Map<Long, T> map = new HashMap<>(16);
        for (T t : list) {
            Long id = t.getId();
            if (id != null) {
                map.put(id, t);
            }
        }
        return map;
    }

}
