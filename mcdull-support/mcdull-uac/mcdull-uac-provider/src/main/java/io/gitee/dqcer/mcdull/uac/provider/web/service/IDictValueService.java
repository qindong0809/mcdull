package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictValueVO;

/**
 * dict value service
 *
 * @author dqcer
 * @since 2024/04/28
 */
public interface IDictValueService {

    /**
     * 获取列表
     *
     * @param dto dto
     * @return {@link PagedVO}<{@link DictValueVO}>
     */
    PagedVO<DictValueVO> getList(DictValueQueryDTO dto);

    /**
     * 插入
     *
     * @param dto dto
     */
    void insert(DictValueAddDTO dto);
}
