package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ChangeLogUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ChangeLogAndVersionVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ChangeLogVO;

import java.util.List;

/**
 * Change log service
 *
 * @author dqcer
 * @since 2024/7/25 9:19
 */

public interface IChangeLogService {

    /**
     * 加
     *
     * @param addForm 添加表单
     * @return boolean
     */
    boolean add(ChangeLogAddDTO addForm);

    /**
     * 更新
     *
     * @param logUpdateForm 日志更新表单
     */
    void update(ChangeLogUpdateDTO logUpdateForm);

    /**
     * 批量删除
     *
     * @param idList ID 列表
     */
    void batchDelete(List<Integer> idList);

    /**
     * Query 页面
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link ChangeLogVO }>
     */
    PagedVO<ChangeLogVO> queryPage(ChangeLogQueryDTO dto);

    /**
     * 按 ID 获取
     *
     * @param id 身份证
     * @return {@link ChangeLogVO }
     */
    ChangeLogVO getById(Integer id);

    /**
     * 获取更改日志和版本
     *
     * @return {@link ChangeLogAndVersionVO }
     */
    ChangeLogAndVersionVO getChangeLogAndVersion();

    /**
     * 导出数据
     *
     * @param dto DTO
     * @return boolean
     */
    boolean exportData(ChangeLogQueryDTO dto);
}
