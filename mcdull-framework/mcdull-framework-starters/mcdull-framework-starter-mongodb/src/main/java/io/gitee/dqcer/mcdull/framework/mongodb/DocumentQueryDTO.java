package io.gitee.dqcer.mcdull.framework.mongodb;


import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import org.bson.conversions.Bson;

public class DocumentQueryDTO implements DTO {

    private String collectionName;

    /**
     * 过滤器
     * //单条件查询
     * Bson filter = Filters.eq("uid", "123");
     *
     * //多条件查询
     * Bson filter = Filters.and(Filters.eq("uid", "123"), Filters.eq("age", 12));
     *
     * 也可支持or、in等操作符
     */
    private Bson filter;

//    private SortParam sortParam;


    public String getCollectionName() {
        return collectionName;
    }

    public DocumentQueryDTO setCollectionName(String collectionName) {
        this.collectionName = collectionName;
        return this;
    }

    public Bson getFilter() {
        return filter;
    }

    public DocumentQueryDTO setFilter(Bson filter) {
        this.filter = filter;
        return this;
    }
}
