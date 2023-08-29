package io.gitee.dqcer.mcdull.admin.web.service.database.impl;

import io.gitee.dqcer.mcdull.admin.model.convert.database.ConfigEnvConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.database.ConfigEnvLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.ConfigEnvDO;
import io.gitee.dqcer.mcdull.admin.model.vo.database.ConfigEnvVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IConfigEnvRepository;
import io.gitee.dqcer.mcdull.admin.web.service.database.IConfigEnvService;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
*  业务实现类
*
* @author dqcer
* @since 2023-08-29
*/
@Service
public class ConfigEnvServiceImpl implements IConfigEnvService {

    private static final Logger log = LoggerFactory.getLogger(ConfigEnvServiceImpl.class);

    @Resource
    private IConfigEnvRepository configEnvRepository;



    /**
     * 详情
     *
     * @param id 主键
     * @return {@link Result<ConfigEnvVO> }
     */
    @Transactional(readOnly = true)
    @Override
    public Result<ConfigEnvVO> detail(Long id) {
        ConfigEnvDO entity = configEnvRepository.getById(id);
        if (null == entity) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        return Result.ok(ConfigEnvConvert.convertToConfigEnvVO(entity));
    }

    /**
     * 更新
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> update(ConfigEnvLiteDTO dto) {
        Long id = dto.getId();

        ConfigEnvDO dbData = configEnvRepository.getById(id);
        if(null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        ConfigEnvDO entity = ConfigEnvConvert.convertToConfigEnvDO(dto);
        entity.setUpdatedBy(UserContextHolder.currentUserId());
        boolean success = configEnvRepository.updateById(entity);
        if (!success) {
            log.error("数据更新失败, entity:{}", entity);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return Result.ok(id);
    }
}
