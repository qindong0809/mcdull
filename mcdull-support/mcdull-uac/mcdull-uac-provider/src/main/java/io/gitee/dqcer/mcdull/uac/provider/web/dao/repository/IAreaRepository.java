package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.AreaEntity;

import java.util.List;


/**
 * Area 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
public interface IAreaRepository extends IService<AreaEntity> {


    List<AreaEntity> queryListByIds(List<Integer> idList);

    Page<AreaEntity> selectPage(AreaQueryDTO dto);

    List<AreaEntity> getByAreaType(int areaType);

    List<AreaEntity> getByPid(Integer pid);

    AreaEntity getCode(String code);
}