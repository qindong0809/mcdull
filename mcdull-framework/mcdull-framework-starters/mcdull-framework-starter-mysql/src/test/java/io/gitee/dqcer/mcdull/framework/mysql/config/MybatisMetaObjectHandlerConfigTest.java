package io.gitee.dqcer.mcdull.framework.mysql.config;

import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MybatisMetaObjectHandlerConfigTest {

    private final MybatisMetaObjectHandlerConfig handler = new MybatisMetaObjectHandlerConfig();

    @BeforeEach
    void setUp() {
        UserContextHolder.setDefaultSession();
    }

    @AfterEach
    void tearDown() {
        UserContextHolder.clearSession();
    }

    @Test
    void explicitCreatedByIsNotOverwritten() {
        BaseEntity<Object> entity = new BaseEntity<>();
        entity.setCreatedBy(999);

        handler.insertFill(metaObject(entity));

        assertEquals(999, entity.getCreatedBy());
    }

    @Test
    void missingCreatedByIsFilledFromSession() {
        BaseEntity<Object> entity = new BaseEntity<>();

        handler.insertFill(metaObject(entity));

        assertEquals(UserContextHolder.userId(), entity.getCreatedBy());
    }

    private MetaObject metaObject(BaseEntity<?> entity) {
        return MetaObject.forObject(entity, new DefaultObjectFactory(),
                new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
    }
}
