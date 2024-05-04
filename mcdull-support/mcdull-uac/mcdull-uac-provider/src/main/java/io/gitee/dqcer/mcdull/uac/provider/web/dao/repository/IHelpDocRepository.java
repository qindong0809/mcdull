package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocCatalogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.TableColumnEntity;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface IHelpDocRepository extends IService<HelpDocEntity>  {

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List< HelpDocEntity >}
    */
    List<HelpDocEntity> queryListByIds(List<Long> idList);

   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page< HelpDocEntity >}
    */
    Page<HelpDocEntity> selectPage(HelpDocQueryDTO param);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link HelpDocEntity}
     */
    HelpDocEntity getById(Long id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    Long insert(HelpDocEntity entity);

    /**
     * 批量删除
     *
     * @param ids 主键集
     */
    void deleteBatchByIds(List<Long> ids);

    /**
     * 是否存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
    boolean exist(HelpDocEntity entity);

   List<HelpDocEntity> selectList(Long userId);

    List<HelpDocEntity> listByCatalogId(Long helpDocCatalogId);
}