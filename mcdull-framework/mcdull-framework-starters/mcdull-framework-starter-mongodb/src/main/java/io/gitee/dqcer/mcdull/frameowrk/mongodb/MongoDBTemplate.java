package io.gitee.dqcer.mcdull.frameowrk.mongodb;

import io.gitee.dqcer.mcdull.frameowrk.mongodb.wrapper.CriteriaVo;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import org.bson.conversions.Bson;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public List<UnifySession> queryData(DocumentQueryDTO queryDTO) {
        return mongoTemplate.find(new Query(), UnifySession.class, queryDTO.getCollectionName());
    }

    /**
     * 查询单条记录
     *
     * @param collectionName
     * @param uid
     * @return
     */
    @Override
    public <T> T selectOne(String collectionName, String uid) {
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
