package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictValueVO;

import java.util.List;

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

    /**
     * update
     *
     * @param dto dto
     */
    void update(DictValueUpdateDTO dto);

    /**
     * delete
     *
     * @param idList id列表
     */
    void delete(List<Long> idList);

    /**
     * key code
     *
     * @param keyCode keyCode
     * @return {@link List}<{@link DictValueVO}>
     */
    List<DictValueVO> selectByKeyCode(String keyCode);
}
