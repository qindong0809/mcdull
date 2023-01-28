package io.gitee.dqcer.mcdull.frameowrk.mongodb;

import org.bson.conversions.Bson;

import java.util.List;

public interface MongoDBService {

    /**
     * 查询记录
     * @param queryDTO
     * @return
     */
    List<Object> queryData(DocumentQueryDTO queryDTO);

    /**
     * 查询单条记录
     * @param collectionName
     * @param uid
     * @return
     */
    <T> T selectOne(String collectionName, String uid);

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
