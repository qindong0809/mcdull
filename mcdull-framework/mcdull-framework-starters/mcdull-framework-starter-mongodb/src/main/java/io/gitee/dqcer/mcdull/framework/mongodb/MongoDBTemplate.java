package io.gitee.dqcer.mcdull.framework.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MongoDBTemplate implements MongoDBService{

    @Resource
    private MongoTemplate mongoTemplate;


    /**
     * 查询记录
     *
     * @param queryDTO
     * @return
     */
    @Override
    public <T> List<T> queryData(DocumentQueryDTO queryDTO, Class<T> entityClass) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(queryDTO.getCollectionName());
        FindIterable<Document> documents = collection.find(queryDTO.getFilter());
        List<T> list = new ArrayList<>();
        documents.forEach(item -> {
            list.add((T) item);
        });
        return list;
    }

    /**
     * 查询单条记录
     *
     * @param queryDTO    查询dto
     * @param entityClass 实体类
     * @return {@link T}
     */
    @Override
    public <T> T selectOne(DocumentQueryDTO queryDTO, Class<T> entityClass) {
        List<T> list = queryData(queryDTO, entityClass);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 单条记录插入
     *
     * @param collectionName
     * @param jsonObject
     * @return
     */
    @Override
    public <T> boolean insertOrUpdate(String collectionName, T jsonObject) {
        mongoTemplate.insert(jsonObject, collectionName);
        return true;
    }

    /**
     * 批量插入
     *
     * @param collectionName
     * @param jsonArray
     * @return
     */
    @Override
    public <T> boolean insertOrUpdateMany(String collectionName, List<T> jsonArray) {
        return false;
    }

    /**
     * 删除文档
     *
     * @param collectionName
     * @param uid
     * @return
     */
    @Override
    public boolean removeDocument(String collectionName, String uid) {
        return false;
    }

    /**
     * 根据条件删除
     *
     * @param collectionName
     * @param filter
     * @return
     */
    @Override
    public boolean removeDocumentByFilter(String collectionName, Bson filter) {
        return false;
    }
}
