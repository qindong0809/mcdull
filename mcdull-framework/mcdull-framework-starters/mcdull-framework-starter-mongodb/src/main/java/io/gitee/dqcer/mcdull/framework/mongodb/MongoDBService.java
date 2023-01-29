package io.gitee.dqcer.mcdull.framework.mongodb;

import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import org.bson.conversions.Bson;

import java.util.List;

public interface MongoDBService {

    /**
     * 查询记录
     *
     * @param queryDTO    查询dto
     * @param entityClass 实体类
     * @return {@link List}<{@link T}>
     */
    <T> List<T> queryData(DocumentQueryDTO queryDTO, Class<T> entityClass);

    /**
     * 查询单条记录
     *
     * @param queryDTO    查询dto
     * @param entityClass 实体类
     * @return {@link T}
     */
    <T> T selectOne(DocumentQueryDTO queryDTO, Class<T> entityClass);

    /**
     * 分页查询
     * @param queryPageDTO
     * @return
     */
//    PageResult selectPage(DocumentQueryPageDTO queryPageDTO);

    /**
     * 单条记录插入
     * @param collectionName
     * @param jsonObject
     * @return
     */
    <T> boolean insertOrUpdate(String collectionName, T jsonObject);

    /**
     * 批量插入
     * @param collectionName
     * @param jsonArray
     * @return
     */
    <T> boolean insertOrUpdateMany(String collectionName, List<T> jsonArray);

    /**
     * 删除文档
     * @param collectionName
     * @param uid
     * @return
     */
    boolean removeDocument(String collectionName, String uid);

    /**
     * 根据条件删除
     * @param collectionName
     * @param filter
     * @return
     */
    boolean removeDocumentByFilter(String collectionName, Bson filter);
}
