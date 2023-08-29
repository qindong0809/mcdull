package io.gitee.dqcer.mcdull.admin.web.dao.repository.database;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.entity.database.ConfigEnvDO;

/**
*  数据库操作封装接口层
*
* @author dqcer
* @since 2023-08-29
*/
public interface IConfigEnvRepository extends IService<ConfigEnvDO>  {

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ConfigEnvDO}
     */
    ConfigEnvDO getById(Long id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    Long insert(ConfigEnvDO entity);


    /**
     * 是否存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
    boolean exist(ConfigEnvDO entity);
}