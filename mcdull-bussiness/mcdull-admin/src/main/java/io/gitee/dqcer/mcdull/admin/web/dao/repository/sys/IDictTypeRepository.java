package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictTypeDO;

import java.util.List;

/**
* 字典类型 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface IDictTypeRepository extends IService<DictTypeDO>  {

    Page<DictTypeDO> selectPage(DictTypeLiteDTO dto);

    List<DictTypeDO> getListByName(String name);
}