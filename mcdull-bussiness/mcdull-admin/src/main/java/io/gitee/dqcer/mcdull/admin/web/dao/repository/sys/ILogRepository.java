package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.LogEntity;

import java.util.List;

/**
* 日志记录 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface ILogRepository extends IService<LogEntity>  {

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List< LogEntity >}
    */
    List<LogEntity> queryListByIds(List<Long> idList);

   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page< LogEntity >}
    */
    Page<LogEntity> selectPage(LogLiteDTO param);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link LogEntity}
     */
    LogEntity getById(Long id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    Long insert(LogEntity entity);

    /**
     * 通过主键删除
     *
     * @param id 主键
     * @return int 受影响的行数
     */
    int deleteById(Long id);

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
    boolean exist(LogEntity entity);
}