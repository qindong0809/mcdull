package io.gitee.dqcer.mcdull.business.common.mysql;

import java.util.ArrayList;
import java.util.List;

public interface AssembledDatabaseMetadata {

    List<Table> tables = new ArrayList<>();

    List<Index> indices = new ArrayList<>();

    default List<Table> getTables() {
        return tables;
    }

    default List<Index> getIndices() {
        return indices;
    }
}
