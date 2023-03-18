package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictDataDO;

import java.util.List;

/**
* 字典类型 数据库操作封装接口层
*
* @author dqcer
* @since 2023-01-14
*/
public interface IDictDataRepository extends IService<DictDataDO>  {

    List<DictDataDO> dictType(String dictType);

    Page<DictDataDO> selectPage(DictDataLiteDTO dto);
}