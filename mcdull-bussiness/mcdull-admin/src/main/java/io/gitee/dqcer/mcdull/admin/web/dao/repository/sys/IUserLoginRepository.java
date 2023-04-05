package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.LoginInfoLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserLoginDO;

/**
 * 用户登录信息 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IUserLoginRepository extends IService<UserLoginDO> {


    /**
     * 分页
     *
     * @param dto dto
     * @return {@link Page}<{@link UserLoginDO}>
     */
    Page<UserLoginDO> paged(LoginInfoLiteDTO dto);
}
