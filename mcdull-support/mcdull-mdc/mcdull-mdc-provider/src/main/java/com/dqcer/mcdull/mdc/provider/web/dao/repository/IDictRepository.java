package io.gitee.dqcer.mdc.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mdc.provider.model.dto.DictLiteDTO;
import io.gitee.dqcer.mdc.provider.model.entity.DictDO;

/**
 * 码表 数据库操作封装接口层
 *
 * @author dqcer
 * @version 2022/12/25
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
