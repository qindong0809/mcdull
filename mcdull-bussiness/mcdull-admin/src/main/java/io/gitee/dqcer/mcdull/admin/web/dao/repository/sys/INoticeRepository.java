package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.NoticeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.NoticeEntity;

import java.util.List;

/**
* 通知公告表 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-19
*/
public interface INoticeRepository extends IService<NoticeEntity>  {

    boolean checkBusinessUnique(NoticeEntity entity);

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List< NoticeEntity >}
    */
    List<NoticeEntity> queryListByIds(List<Long> idList);

   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page< NoticeEntity >}
    */
    Page<NoticeEntity> selectPage(NoticeLiteDTO param);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link NoticeEntity}
     */
    NoticeEntity getById(Long id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Long id
     */
    Long insert(NoticeEntity entity);

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

}