package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictKeyVO;

import java.util.List;

/**
 * dict key service
 *
 * @author dqcer
 * @since 2024/04/28
 */
public interface IDictKeyService {

    /**
     * 查询全部
     *
     * @return {@link List}<{@link DictKeyVO}>
     */
    List<DictKeyVO> queryAll();

    /**
     * 获取列表
     *
     * @param dto dto
     * @return {@link PagedVO}<{@link DictKeyVO}>
     */
    PagedVO<DictKeyVO> getList(DictKeyQueryDTO dto);

    /**
     * 插入
     *
     * @param dto dto
     */
    void insert(DictKeyAddDTO dto);

    /**
     * delete
     *
     * @param idList id列表
     */
    void delete(List<Integer> idList);

    /**
     * update
     *
     * @param dto dto
     */
    void update(DictKeyUpdateDTO dto);

    /**
     * get by code
     *
     * @param keyCode key code
     * @return {@link DictKeyVO}
     */
    DictKeyVO getByCode(String keyCode);
}
