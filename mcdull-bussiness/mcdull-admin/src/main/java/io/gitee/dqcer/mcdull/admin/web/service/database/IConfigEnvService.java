package io.gitee.dqcer.mcdull.admin.web.service.database;

import io.gitee.dqcer.mcdull.admin.model.dto.database.ConfigEnvLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.ConfigEnvVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

/**
*  业务接口类
*
* @author dqcer
* @since 2023-08-29
*/
public interface IConfigEnvService {

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return {@link Result<ConfigEnvVO> }
     */
    Result<ConfigEnvVO> detail(Long id);

    /**
     * 编辑数据
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    Result<Long> update(ConfigEnvLiteDTO dto);

}
