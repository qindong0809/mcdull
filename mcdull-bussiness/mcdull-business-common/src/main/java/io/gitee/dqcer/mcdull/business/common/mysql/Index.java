package io.gitee.dqcer.mcdull.business.common.mysql;

import cn.hutool.core.util.StrUtil;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Index {

    String tableName;

    String indexName;

    boolean unique;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    String params;

    public void addParams(String param) {
        if (StrUtil.isBlank(params)) {
            params = param;
        } else {
            params = params + "," + param;
        }
    }

}
