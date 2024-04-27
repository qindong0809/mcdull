package io.gitee.dqcer.mcdull.framework.base.engine;

import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;

import java.util.List;

/**
 * Compare Bean
 *
 * @author dqcer
 * @since 2024/02/05
 */
public class CompareBean<T extends IdEntity<PK>, PK> {

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
