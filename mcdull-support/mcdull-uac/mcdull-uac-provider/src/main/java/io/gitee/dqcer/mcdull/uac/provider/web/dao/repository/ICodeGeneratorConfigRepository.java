package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FeedbackQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.CodeGeneratorConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;

import java.util.List;

/**
* @author dqcer
* @since 2024-04-29
*/
public interface ICodeGeneratorConfigRepository extends IService<CodeGeneratorConfigEntity>  {

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List< CodeGeneratorConfigEntity >}
    */
    List<CodeGeneratorConfigEntity> queryListByIds(List<Long> idList);

   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page< CodeGeneratorConfigEntity >}
    */
    Page<CodeGeneratorConfigEntity> selectPage(FeedbackQueryDTO param);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ConfigEntity}
     */
    CodeGeneratorConfigEntity getById(Long id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    Long insert(CodeGeneratorConfigEntity entity);

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
    boolean exist(CodeGeneratorConfigEntity entity);
}