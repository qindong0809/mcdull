package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.DictDO;

/**
 * 码表 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/25
 */
public interface IDictRepository extends IService<DictDO> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link DictDO}>
     */
    Page<DictDO> selectPage(DictLiteDTO dto);
}
