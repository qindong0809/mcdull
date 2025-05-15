package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FileQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FileEntity;

import java.util.List;

/**
* File Repository
*
* @author dqcer
* @since 2024-04-29
*/
public interface IFileRepository extends IRepository<FileEntity> {

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List}
    */
    List<FileEntity> queryListByIds(List<Integer> idList);

   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page }
    */
    Page<FileEntity> selectPage(ConfigQueryDTO param);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ConfigEntity}
     */
    FileEntity getById(Integer id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Integer id
     */
    Integer insert(FileEntity entity);

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
    boolean exist(FileEntity entity);

    /**
     * 分页查询
     *
     * @param dto         dto
     * @param userIdList  用户id列表
     * @param childerList
     * @return {@link Page }
     */
    Page<FileEntity> selectPage(FileQueryDTO dto, List<Integer> userIdList, List<Integer> childerList);

    /**
     * 根据文件key获取文件信息
     *
     * @param fileKey 文件key
     * @return {@link FileEntity}
     */
    FileEntity getByFileKey(String fileKey);
}