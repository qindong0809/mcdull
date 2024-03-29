package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserEntity;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;

import java.util.List;

/**
 * 用户 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IUserRepository extends IService<UserEntity> {

    Page<UserEntity> selectPage(UserLiteDTO dto);

    Long insert(UserEntity entity);

    UserEntity oneByAccount(String account);

    List<UserPowerVO> queryResourceModules(Long userId);


    void updateLoginTimeById(Long userId);

    UserEntity queryUserByAccount(String account);

    void delete(Long id);

    void updateStatusById(Long id, String status);
}
