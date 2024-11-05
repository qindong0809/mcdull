package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;

import java.util.List;

/**
 * Login Log Repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ILoginLogRepository extends IRepository<LoginLogEntity> {

   /**
    * 根据ID列表批量查询数据
    *
    * @param idList id列表
    * @return {@link List}
    */
    List<LoginLogEntity> queryListByIds(List<Integer> idList);

   /**
    * 按条件分页查询
    *
    * @param param 参数
    * @return {@link Page }
    */
    Page<LoginLogEntity> selectPage(LoginLogQueryDTO param);

    /**
     * 根据ID获取单条数据
     *
     * @param id 主键
     * @return {@link ConfigEntity}
     */
    LoginLogEntity getById(Integer id);

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return Integer id
     */
    Integer insert(LoginLogEntity entity);

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
    boolean exist(LoginLogEntity entity);

 /**
  * 按用户 ID 获取列表
  *
  * @param loginName 登录名称
  * @return {@link List }<{@link LoginLogEntity }>
  */
 List<LoginLogEntity> getListByLoginName(String loginName);
}