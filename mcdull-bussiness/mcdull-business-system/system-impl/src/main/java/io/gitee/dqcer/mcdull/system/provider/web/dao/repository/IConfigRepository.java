package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.ConfigEntity;

import java.util.List;

/**
* Config repository
*
* @author dqcer
* @since 2024-04-29
*/
public interface IConfigRepository extends IRepository<ConfigEntity> {

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List}
    */
    List<ConfigEntity> queryListByIds(List<Integer> idList);

   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page}
    */
    Page<ConfigEntity> selectPage(ConfigQueryDTO param);

    /**
     * 选择列表
     *
     * @param param 参数
     * @return {@link List }<{@link ConfigEntity }>
     */
    List<ConfigEntity> selectList(ConfigQueryDTO param);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ConfigEntity}
     */
    ConfigEntity getById(Integer id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Integer id
     */
    Integer insert(ConfigEntity entity);

    /**
     * 批量删除
     *
     * @param ids 主键集
     */
    void deleteBatchByIds(List<Integer> ids);

    /**
     * 是否存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
    boolean exist(ConfigEntity entity);

    /**
     * 根据key获取单条数据
     *
     * @param key key
     * @return {@link ConfigEntity}
     */
    ConfigEntity selectOne(String key);
}