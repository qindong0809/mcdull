package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictTypeEntity;

import java.util.List;

/**
* 字典类型 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface IDictTypeRepository extends IService<DictTypeEntity>  {

    Page<DictTypeEntity> selectPage(DictTypeLiteDTO dto);

    List<DictTypeEntity> getListByName(String name);

    void removeUpdateById(Long id);
}