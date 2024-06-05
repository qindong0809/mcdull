package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginFailQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLockedEntity;

/**
* 数据库操作封装接口层
*
* @author dqcer
* @since 2024-04-29
*/
public interface ILoginLockedRepository extends IService<LoginLockedEntity>  {


    LoginLockedEntity get(String loginName);

    Page<LoginLockedEntity> selectPage(LoginFailQueryDTO dto);
}