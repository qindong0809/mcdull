package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.AreaEntity;

import java.util.List;


/**
 * Area repository
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
public interface IAreaRepository extends IRepository<AreaEntity> {

    /**
     * 查询列表
     *
     * @param idList idList
     * @return {@link List}<{@link AreaEntity}>
     */
    List<AreaEntity> queryListByIds(List<Integer> idList);

    /**
     * 分页
     *
     * @param dto dto
     * @return {@link Page}<{@link AreaEntity}>
     */
    Page<AreaEntity> selectPage(AreaQueryDTO dto);

    /**
     * 根据类型获取地区
     *
     * @param areaType areaType
     * @return {@link List}<{@link AreaEntity}>
     */
    List<AreaEntity> getByAreaType(int areaType);

    /**
     * 根据父id获取地区
     *
     * @param pid pid
     * @return {@link List}<{@link AreaEntity}>
     */
    List<AreaEntity> getByPid(Integer pid);

    /**
     * 根据code获取地区
     *
     * @param code code
     * @return {@link AreaEntity}
     */
    AreaEntity getCode(String code);
}