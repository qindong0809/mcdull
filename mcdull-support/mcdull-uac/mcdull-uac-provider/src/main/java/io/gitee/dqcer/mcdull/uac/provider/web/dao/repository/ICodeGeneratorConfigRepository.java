package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FeedbackQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.TableQueryForm;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.CodeGeneratorConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableColumnVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableVO;

import java.util.List;

/**
 * Code generator config repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ICodeGeneratorConfigRepository extends IRepository<CodeGeneratorConfigEntity> {

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List}
    */
    List<CodeGeneratorConfigEntity> queryListByIds(List<Integer> idList);

   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page}
    */
    Page<CodeGeneratorConfigEntity> selectPage(FeedbackQueryDTO param);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ConfigEntity}
     */
    CodeGeneratorConfigEntity getById(Integer id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     */
    void insert(CodeGeneratorConfigEntity entity);

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
    boolean exist(CodeGeneratorConfigEntity entity);

    /**
     * 根据表名获取表字段
     *
     * @param table 表名
     * @return {@link List}
     */
    List<TableColumnVO> getByTable(String table);

    /**
     * 按条件分页查询
     *
     * @param dto 参数
     * @return {@link Page}
     */
    List<TableVO> queryTableList(Page<?> page, TableQueryForm dto);

    /**
     * 是否存在
     *
     * @param tableName 表名
     * @return boolean true/存在 false/不存在
     */
    boolean existByTable(String tableName);

    /**
     * 获取表配置
     *
     * @param tableName 表名
     * @return {@link CodeGeneratorConfigEntity}
     */
    CodeGeneratorConfigEntity getTableConfig(String tableName);
}