package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserConfigEntity;


/**
 * User 配置存储库
 *
 * @author dqcer
 * @since 2025/03/21
 */
public interface IUserConfigRepository extends IRepository<UserConfigEntity> {

    /**
     * 保存或更新
     *
     * @param userId     用户 ID
     * @param dateFormat 日期格式
     * @param timezone   时区
     */
    void saveOrUpdate(Integer userId, String dateFormat, String timezone, Boolean appendTimezoneStyle);

    /**
     * 按用户 ID 获取
     *
     * @param userId 用户 ID
     * @return {@link UserConfigEntity }
     */
    UserConfigEntity getByUserId(Integer userId);
}