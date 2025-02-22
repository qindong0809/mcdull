package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.ConfigAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.ConfigConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ConfigInfoVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IConfigRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IConfigService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
* Config ServiceImpl
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class ConfigServiceImpl
        extends BasicServiceImpl<IConfigRepository> implements IConfigService {

    @Resource
    private ICommonManager commonManager;
    @Resource
    private IAuditManager auditManager;

    @Override
    public PagedVO<ConfigInfoVO> queryPage(ConfigQueryDTO dto) {
        Page<ConfigEntity> entityPage = baseRepository.selectPage(dto);
        List<ConfigInfoVO> voList = new ArrayList<>();
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
        ConfigEntity configEntity = ConfigConvert.convertToConfigEntity(dto);
        Integer configId = baseRepository.insert(configEntity);
        auditManager.saveByAddEnum(dto.getConfigName(), configId, this.buildAuditLog(configEntity));
    }

    private Audit buildAuditLog(ConfigEntity configEntity) {
        ConfigAudit audit = new ConfigAudit();
        audit.setConfigName(configEntity.getConfigName());
        audit.setConfigValue(configEntity.getConfigValue());
        audit.setConfigKey(configEntity.getConfigKey());
        audit.setRemark(configEntity.getRemark());
        return audit;
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
        auditManager.saveByUpdateEnum(dto.getConfigName(), configId,
                this.buildAuditLog(configEntity), this.buildAuditLog(baseRepository.getById(configId)));
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
        for (ConfigEntity entity : entityList) {
            auditManager.saveByDeleteEnum(entity.getConfigName(), entity.getId(), null);
        }
    }

    @Override
    public void exportData(ConfigQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, "参数配置", this.getTitleMap(), this::convertMap);
    }

    private Map<String, String> convertMap(ConfigInfoVO configInfoVO) {
        Map<String, String> map = new HashMap<>(8);
        map.put("configKey", configInfoVO.getConfigKey());
        map.put("configName", configInfoVO.getConfigName());
        map.put("configValue", configInfoVO.getConfigValue());
        map.put("remark", configInfoVO.getRemark());
        map.put("createTime", commonManager.convertDateTimeStr(configInfoVO.getCreateTime()));
        map.put("updateTime", commonManager.convertDateTimeStr(configInfoVO.getUpdateTime()));
        return map;
    }

    private Map<String, String> getTitleMap() {
        Map<String, String> titleMap = new HashMap<>(8);
        titleMap.put("参数key", "configKey");
        titleMap.put("参数名称", "configName");
        titleMap.put("参数值", "configValue");
        titleMap.put("备注", "remark");
        titleMap.put("创建时间", "createTime");
        titleMap.put("更新时间", "updateTime");
        return titleMap;
    }


}
