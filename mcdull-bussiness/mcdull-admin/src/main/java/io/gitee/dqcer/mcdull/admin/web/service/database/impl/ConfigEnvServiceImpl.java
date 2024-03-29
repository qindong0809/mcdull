package io.gitee.dqcer.mcdull.admin.web.service.database.impl;

import cn.hutool.core.util.ObjectUtil;
import io.gitee.dqcer.mcdull.admin.model.convert.database.ConfigEnvConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.database.ConfigEnvLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.ConfigEnvEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.database.ConfigEnvVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IConfigEnvRepository;
import io.gitee.dqcer.mcdull.admin.web.service.database.IConfigEnvService;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
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



    @Transactional(readOnly = true)
    @Override
    public Result<ConfigEnvVO> detail(Integer type) {
        ConfigEnvEntity entity = configEnvRepository.getByType(type);
        if (null == entity) {
            return Result.success();
        }
        return Result.success(ConfigEnvConvert.convertToConfigEnvVO(entity));
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
        ConfigEnvEntity configEnv = ConfigEnvConvert.convertToConfigEnvDO(dto);
        if (ObjectUtil.isNull(id)) {
            configEnvRepository.insert(configEnv);
            return Result.success(configEnv.getId());
        }
        ConfigEnvEntity dbData = configEnvRepository.getById(id);
        if(null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        boolean success = configEnvRepository.updateById(configEnv);
        if (!success) {
            log.error("数据更新失败, entity:{}", configEnv);
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
        return Result.success(id);
    }
}
