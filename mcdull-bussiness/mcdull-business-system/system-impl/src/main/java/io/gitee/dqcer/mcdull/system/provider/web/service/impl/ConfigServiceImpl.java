package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.system.provider.model.audit.ConfigAudit;
import io.gitee.dqcer.mcdull.system.provider.model.convert.ConfigConvert;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ConfigInfoVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.ConfigMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IConfigService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFileService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


/**
* Config ServiceImpl
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class ConfigServiceImpl
        extends BasicCurdServiceImpl<ConfigMapper, ConfigEntity> implements IConfigService {

    @Resource
    private ICommonManager commonManager;
    @Resource
    private IAuditManager auditManager;
    @Resource
    private IFileService fileService;


    @Override
    public PagedVO<ConfigInfoVO> queryPage(ConfigQueryDTO dto) {
        List<ConfigInfoVO> voList = new ArrayList<>();
        List<ConfigEntity> records = this.selectList(dto);
        if (CollUtil.isNotEmpty(records)) {
            List<Integer> idList = records.stream().map(IdEntity::getId).collect(Collectors.toList());
            Map<Integer, List<FileEntity>> fileMap = commonManager.getFileList(idList, ConfigEntity.class);
            for (ConfigEntity entity : records) {
                ConfigInfoVO vo = ConfigConvert.convertToConfigVO(entity);
                List<FileEntity> fileEntityList = fileMap.get(entity.getId());
                if (CollUtil.isNotEmpty(fileEntityList)) {
                    vo.setAttachmentName(fileEntityList.stream().map(FileEntity::getFileName).collect(Collectors.joining(",")));
                }
                String attachmentName = dto.getAttachmentName();
                if (StrUtil.isNotBlank(attachmentName)) {
                    if (!StrUtil.containsIgnoreCase(vo.getAttachmentName(), attachmentName)) {
                        continue;
                    }
                }
                voList.add(vo);
            }
        }
        return  PageUtil.ofSub(voList, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(ConfigAddDTO dto) {
        List<ConfigEntity> list = super.list();
        if (CollUtil.isNotEmpty(list)) {
            LogicCheckUtil.validNameExist(null, dto.getConfigName(),
                    list, entity -> entity.getConfigName().equals(dto.getConfigName()));
        }
        ConfigEntity configEntity = ConfigConvert.convertToConfigEntity(dto);
        Integer configId = baseMapper.insert(configEntity);
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
        ConfigEntity configEntity = super.getById(configId);
        if (ObjUtil.isNull(configEntity)) {
            LogicCheckUtil.throwDataNotExistException(configId);
        }
        List<ConfigEntity> list = super.list();
        if (CollUtil.isNotEmpty(list)) {
            LogicCheckUtil.validNameExist(configId, dto.getConfigName(),
                    list, entity -> (!entity.getId().equals(configId))
                            && entity.getConfigName().equals(dto.getConfigName()));
        }
        ConfigEntity updateEntity = ConfigConvert.convertToConfigEntity(dto);
        updateEntity.setId(configId);
        super.updateById(updateEntity);
        auditManager.saveByUpdateEnum(dto.getConfigName(), configId,
                this.buildAuditLog(configEntity), this.buildAuditLog(super.getById(configId)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> idList) {
        List<ConfigEntity> entityList = this.listByIds(idList);
        if (CollUtil.isEmpty(entityList)) {
            entityList = new LinkedList<>();
        }
        if (idList.size() != entityList.size()) {
            LogicCheckUtil.throwDataNotExistException(idList);
        }
        this.deleteBatchByIds(idList);

        for (ConfigEntity entity : entityList) {
            fileService.remove(entity.getId(), ConfigEntity.class);
            auditManager.saveByDeleteEnum(entity.getConfigName(), entity.getId(), null);
        }
    }

    @Override
    public boolean exportData(ConfigQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryPage, StrUtil.EMPTY, this.getTitleList());
        return true;
    }

    @Override
    public Boolean exportAttachmentData(Integer id) {
        List<Pair<String, byte[]>> fileDateList = commonManager.getFileDateList(id, ConfigEntity.class);
        if (CollUtil.isNotEmpty(fileDateList)) {
            Pair<String, byte[]> pair = fileDateList.get(0);
            ServletUtil.download(pair.getKey(), pair.getValue());
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importAttachmentData(Integer id, MultipartFile file) {
        ConfigEntity config = super.getById(id);
        if (ObjUtil.isNull(config) || config.getDelFlag()) {
            LogicCheckUtil.throwDataNotExistException(id);
        }
        commonManager.uploadFile(file, id, ConfigEntity.class);
    }


    private List<Pair<String, Func1<ConfigInfoVO, ?>>> getTitleList() {
        List<Pair<String, Func1<ConfigInfoVO, ?>>> titleList = new ArrayList<>(8);
        titleList.add(Pair.of("参数key", ConfigInfoVO::getConfigKey));
        titleList.add(Pair.of("参数名称", ConfigInfoVO::getConfigName));
        titleList.add(Pair.of("参数值", ConfigInfoVO::getConfigValue));
        titleList.add(Pair.of("备注", ConfigInfoVO::getRemark));
        titleList.add(Pair.of("创建时间", ConfigInfoVO::getCreateTime));
        titleList.add(Pair.of("更新时间", ConfigInfoVO::getUpdateTime));
        return titleList;
    }


    public List<ConfigEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<ConfigEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ConfigEntity::getId, idList);
        List<ConfigEntity> list =  baseMapper.selectList(wrapper);
        if (ObjectUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    public Page<ConfigEntity> selectPage(ConfigQueryDTO param) {
        LambdaQueryWrapper<ConfigEntity> lambda = new QueryWrapper<ConfigEntity>().lambda();
        String configKey = param.getConfigKey();
        if (CharSequenceUtil.isNotBlank(configKey)) {
            lambda.like(ConfigEntity::getConfigKey, configKey);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    public List<ConfigEntity> selectList(ConfigQueryDTO param) {
        LambdaQueryWrapper<ConfigEntity> lambda = new QueryWrapper<ConfigEntity>().lambda();
        String configKey = param.getConfigKey();
        if (CharSequenceUtil.isNotBlank(configKey)) {
            lambda.like(ConfigEntity::getConfigKey, configKey);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectList(lambda);
    }

    public ConfigEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    public Integer insert(ConfigEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    public boolean exist(ConfigEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    public ConfigEntity selectOne(String key) {
        LambdaQueryWrapper<ConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(ConfigEntity::getConfigKey, key);
        return baseMapper.selectOne(query);
    }

    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }



}
