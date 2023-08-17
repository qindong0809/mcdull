package io.gitee.dqcer.mcdull.admin.web.dao.repository.database;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.entity.database.TicketDO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.TicketInstanceDO;

import java.util.List;

/**
*  数据库操作封装接口层
*
* @author dqcer
* @since 2023-08-17
*/
public interface ITicketInstanceRepository extends IService<TicketInstanceDO>  {

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List<TicketDO>}
    */
    List<TicketInstanceDO> queryListByIds(List<Long> idList);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link TicketDO}
     */
    TicketInstanceDO getById(Long id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    Long insert(TicketInstanceDO entity);

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
    boolean exist(TicketInstanceDO entity);

    List<TicketInstanceDO> getListByTicketId(Long ticketId);
}