package io.gitee.dqcer.mcdull.admin.web.dao.repository.common;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigDO;

/**
 * 系统配置 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/25
 */
public interface ISysConfigRepository extends IService<SysConfigDO> {

    /**
     * 根据key查找
     *
     * @param key 关键
     * @return {@link String}
     */
    SysConfigDO findByKey(String key);
}
