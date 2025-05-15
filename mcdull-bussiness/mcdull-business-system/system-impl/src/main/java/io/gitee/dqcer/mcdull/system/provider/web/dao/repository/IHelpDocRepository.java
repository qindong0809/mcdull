package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.HelpDocQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.HelpDocEntity;

import java.util.List;

/**
 * Help doc repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IHelpDocRepository extends IRepository<HelpDocEntity> {

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List}
    */
    List<HelpDocEntity> queryListByIds(List<Integer> idList);

   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page}
    */
    Page<HelpDocEntity> selectPage(HelpDocQueryDTO param);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link HelpDocEntity}
     */
    HelpDocEntity getById(Integer id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Integer id
     */
    Integer insert(HelpDocEntity entity);

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
    boolean exist(HelpDocEntity entity);

   List<HelpDocEntity> selectList(Integer userId);

    List<HelpDocEntity> listByCatalogId(Integer helpDocCatalogId);
}