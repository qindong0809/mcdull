package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ConfigInfoVO;

import java.util.List;

/**
 * Config service
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface IConfigService {


    /**
     * Query 页面
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link ConfigInfoVO }>
     */
    PagedVO<ConfigInfoVO> queryPage(ConfigQueryDTO dto);

    /**
     * 加
     *
     * @param dto DTO
     */
    void add(ConfigAddDTO dto);

    /**
     * 更新
     *
     * @param dto DTO
     */
    void update(ConfigUpdateDTO dto);

    /**
     * 删除
     *
     * @param idList ID 列表
     */
    void delete(List<Integer> idList);

    /**
     * 导出数据
     *
     * @param dto DTO
     * @return boolean
     */
    boolean exportData(ConfigQueryDTO dto);
}
