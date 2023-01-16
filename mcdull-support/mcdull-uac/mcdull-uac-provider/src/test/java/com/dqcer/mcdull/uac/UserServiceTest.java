package io.gitee.dqcer.uac;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.framework.base.entity.BaseDO;
import io.gitee.dqcer.uac.provider.UserDataContentApplication;
import io.gitee.dqcer.uac.provider.model.entity.UserDO;
import io.gitee.dqcer.uac.provider.web.dao.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@ActiveProfiles("dev")
@SpringBootTest(classes = {UserDataContentApplication.class})
public class UserServiceTest {

    @Autowired
    private IUserRepository userRepository;


    @Test
    void checkSql() {
        Assertions.assertThrows(MyBatisSystemException.class, () -> {
            userRepository.list();
        }, "mybatis-plus 插件 IllegalSQLInnerInterceptor：检查sql语法是否符合要求 已生效");
    }

    @Test
    void deleteAllTableOrUpdateAllTable() {
        Assertions.assertThrows(MyBatisSystemException.class, () -> {
            LambdaUpdateWrapper<UserDO> update = Wrappers.lambdaUpdate();
            update.set(BaseDO::getUpdatedTime, new Date());
            userRepository.update(null, update);
        }, "mybatis-plus 插件 BlockAttackInnerInterceptor：防止全表更新或删除 已生效");

    }
}
