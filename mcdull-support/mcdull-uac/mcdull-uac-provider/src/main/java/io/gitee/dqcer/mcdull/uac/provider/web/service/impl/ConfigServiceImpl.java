package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.ConfigConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ConfigVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IConfigRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
* 系统配置 业务实现类
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class ConfigServiceImpl extends BasicServiceImpl<IConfigRepository> implements IConfigService {

    @Override
    public PagedVO<ConfigVO> queryPage(ConfigQueryDTO dto) {
        Page<ConfigEntity> entityPage = baseRepository.selectPage(dto);
        List<ConfigVO> voList = new ArrayList<>();
        for (ConfigEntity entity : entityPage.getRecords()) {
            voList.add(ConfigConvert.convertToConfigVO(entity));
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(ConfigAddDTO dto) {
        List<ConfigEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(null, dto.getConfigName(),
                    list, entity -> entity.getConfigName().equals(dto.getConfigName()));
        }
        baseRepository.insert(ConfigConvert.convertToConfigEntity(dto));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ConfigUpdateDTO dto) {
        Integer configId = dto.getConfigId();
        ConfigEntity configEntity = baseRepository.getById(configId);
        if (ObjUtil.isNull(configEntity)) {
            this.throwDataNotExistException(configId);
        }
        List<ConfigEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(configId, dto.getConfigName(),
                    list, entity -> (!entity.getId().equals(configId))
                            && entity.getConfigName().equals(dto.getConfigName()));
        }
        ConfigEntity updateEntity = ConfigConvert.convertToConfigEntity(dto);
        updateEntity.setId(configId);
        baseRepository.updateById(updateEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> idList) {
        List<ConfigEntity> entityList = baseRepository.listByIds(idList);
        if (CollUtil.isEmpty(entityList)) {
            entityList = new LinkedList<>();
        }
        if (idList.size() != entityList.size()) {
            this.throwDataNotExistException(idList);
        }
        baseRepository.deleteBatchByIds(idList);
    }

    @Override
    public String getConfig(String key) {
        ConfigEntity entity = baseRepository.selectOne(key);
        if (ObjUtil.isNotNull(entity)) {
            return entity.getConfigValue();
        }
        return null;
    }
}
