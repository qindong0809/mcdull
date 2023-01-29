package ${cfg.repository};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import ${cfg.apiEntity}.${cfg.entityName};
import ${cfg.apiDto}.${cfg.dtoName};

/**
* ${table.comment!} 数据库操作封装接口层
*
* @author ${author}
* @since ${date}
*/
public interface ${cfg.repositoryName} extends IService<${cfg.entityName}>  {

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List<${cfg.entityName}>}
    */
    List<${cfg.entityName}> queryListByIds(List<Long> idList);

   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page<${cfg.entityName}>}
    */
    Page<${cfg.entityName}> selectPage(${cfg.dtoName} param);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ${cfg.entityName}}
     */
    ${cfg.entityName} getById(Long id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    Long insert(${cfg.entityName} entity);

    /**
     * 通过主键删除
     *
     * @param id 主键
     */
    void deleteById(Long id);

    /**
     * 批量删除
     *
     * @param ids 主键集
     * @return int 受影响的行数
     */
    int deleteBatchIds(List<Long> ids);

    /**
     * 是否存在
     *
     * @param entity 实体对象
     * @return boolean true/存在 false/不存在
     */
    boolean exist(${cfg.entityName} entity);
}