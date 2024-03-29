package io.gitee.dqcer.mcdull.uac;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

//@ActiveProfiles("dev")
//@SpringBootTest(classes = {UserDataContentApplication.class})
public class UserServiceTest {

    @Autowired
    private IUserRepository userRepository;


//    @Test
    void checkSql() {
        Assertions.assertThrows(MyBatisSystemException.class, () -> {
            userRepository.list();
        }, "mybatis-plus 插件 IllegalSQLInnerInterceptor：检查sql语法是否符合要求 已生效");
    }

//    @Test
    void deleteAllTableOrUpdateAllTable() {
        Assertions.assertThrows(MyBatisSystemException.class, () -> {
            LambdaUpdateWrapper<UserEntity> update = Wrappers.lambdaUpdate();
            update.set(BaseEntity::getUpdatedTime, new Date());
            userRepository.update(null, update);
        }, "mybatis-plus 插件 BlockAttackInnerInterceptor：防止全表更新或删除 已生效");

    }
}
