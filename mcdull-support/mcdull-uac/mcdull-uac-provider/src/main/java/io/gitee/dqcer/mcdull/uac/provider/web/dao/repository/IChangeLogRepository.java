package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ChangeLogEntity;

import java.util.List;

/**
* 数据库操作封装接口层
*
* @author dqcer
* @since 2024-04-29
*/
public interface IChangeLogRepository extends IRepository<ChangeLogEntity> {

    /**
     * 查询列表
     *
     * @param idList idList
     * @return {@link List}<{@link ChangeLogEntity}>
     */
    List<ChangeLogEntity> queryListByIds(List<Integer> idList);

    /**
     * 查询分页
     *
     * @param param dto
     * @return {@link Page}<{@link ChangeLogEntity}>
     */
    Page<ChangeLogEntity> selectPage(ChangeLogQueryDTO param);

    /**
     * 获取
     *
     * @param id id
     * @return {@link ChangeLogEntity}
     */
    ChangeLogEntity getById(Integer id);

    /**
     * 插入
     *
     * @param entity entity
     */
    void insert(ChangeLogEntity entity);

    /**
     * 删除
     *
     * @param ids ids
     */
    void deleteBatchByIds(List<Integer> ids);

    /**
     * 存在
     *
     * @param entity entity
     * @return boolean
     */
    boolean exist(ChangeLogEntity entity);
}