package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.mysql.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DatasourceEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantMenuEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantUserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.TenantIsolationModeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.enums.TenantOwnerTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceOptionVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantDetailVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantVO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.TenantCacheEvictManager;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.DatasourceMapper;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.TenantMapper;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.TenantMenuMapper;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.TenantUserMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITenantService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TenantServiceImpl extends BasicCurdServiceImpl<TenantMapper, TenantEntity> implements ITenantService {

    @Resource
    private DatasourceMapper datasourceMapper;
    @Resource
    private TenantUserMapper tenantUserMapper;
    @Resource
    private TenantMenuMapper tenantMenuMapper;
    @Resource
    private TenantCacheEvictManager cacheEvictManager;

    @Override
    public PagedVO<TenantVO> queryPage(TenantQueryDTO dto) {
        LambdaQueryWrapper<TenantEntity> query = new LambdaQueryWrapper<>();
        if (dto.getTenantName() != null && !dto.getTenantName().isEmpty()) {
            query.like(TenantEntity::getTenantName, dto.getTenantName());
        }
        if (dto.getOwnerType() != null) {
            query.eq(TenantEntity::getOwnerType, dto.getOwnerType());
        }
        if (dto.getInitStatus() != null) {
            query.eq(TenantEntity::getInitStatus, dto.getInitStatus());
        }
        if (dto.getDatasourceId() != null) {
            query.eq(TenantEntity::getDatasourceId, dto.getDatasourceId());
        }
        if (dto.getApplicationCode() != null && !dto.getApplicationCode().isEmpty()) {
            query.like(TenantEntity::getApplicationCode, dto.getApplicationCode());
        }
        if (dto.getInactive() != null) {
            query.eq(TenantEntity::getInactive, dto.getInactive());
        }
        query.orderByDesc(TenantEntity::getId);
        Page<TenantEntity> page = baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
        List<TenantVO> list = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        Map<Integer, String> dsNames = datasourceNameMap();
        list.forEach(vo -> vo.setDatasourceName(dsNames.get(vo.getDatasourceId())));
        return PageUtil.toPage(list, page);
    }

    @Override
    public TenantDetailVO detail(Integer id) {
        TenantEntity entity = mustGet(id);
        TenantDetailVO vo = new TenantDetailVO();
        copy(entity, vo);
        vo.setDatasourceName(datasourceNameMap().get(entity.getDatasourceId()));
        vo.setUserCount(Math.toIntExact(tenantUserMapper.selectCount(new LambdaQueryWrapper<TenantUserEntity>()
                .eq(TenantUserEntity::getTenantId, id).eq(TenantUserEntity::getDelFlag, false))));
        vo.setMenuCount(Math.toIntExact(tenantMenuMapper.selectCount(new LambdaQueryWrapper<TenantMenuEntity>()
                .eq(TenantMenuEntity::getTenantId, id).eq(TenantMenuEntity::getDelFlag, false))));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(TenantAddDTO dto) {
        validate(dto, null);
        TenantEntity entity = new TenantEntity();
        apply(dto, entity);
        baseMapper.insert(entity);
        cacheEvictManager.clearTenant(entity.getId(), Collections.emptyList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TenantUpdateDTO dto) {
        TenantEntity entity = mustGet(dto.getId());
        validate(dto, dto.getId());
        apply(dto, entity);
        baseMapper.updateById(entity);
        cacheEvictManager.clearTenant(entity.getId(), tenantUserMapper.selectUserIdsByTenantId(entity.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Integer id, Boolean inactive) {
        TenantEntity entity = mustGet(id);
        entity.setInactive(inactive);
        baseMapper.updateById(entity);
        cacheEvictManager.clearTenant(id, tenantUserMapper.selectUserIdsByTenantId(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        mustGet(id);
        baseMapper.deleteById(id);
        cacheEvictManager.clearTenant(id, tenantUserMapper.selectUserIdsByTenantId(id));
    }

    @Override
    public List<DatasourceOptionVO> datasourceOptions() {
        LambdaQueryWrapper<DatasourceEntity> query = new LambdaQueryWrapper<>();
        query.eq(DatasourceEntity::getInactive, false).eq(DatasourceEntity::getDelFlag, false);
        return datasourceMapper.selectList(query).stream().map(entity -> {
            DatasourceOptionVO vo = new DatasourceOptionVO();
            vo.setId(entity.getId());
            vo.setDatasourceName(entity.getDatasourceName());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public TenantEntity getEntity(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Set<Integer> authorizedTenantIds(Integer userId) {
        return tenantUserMapper.selectTenantIdsByUserId(userId);
    }

    private void validate(TenantAddDTO dto, Integer excludeId) {
        if (!TenantIsolationModeEnum.DATABASE.getCode().equals(dto.getIsolationMode())) {
            throw new BusinessException("system.tenant.isolation.unsupported");
        }
        if (TenantOwnerTypeEnum.TRIAL.getCode().equals(dto.getOwnerType()) && dto.getExpireTime() == null) {
            throw new BusinessException("system.tenant.expire.required");
        }
        LambdaQueryWrapper<TenantEntity> query = new LambdaQueryWrapper<>();
        query.eq(TenantEntity::getTenantName, dto.getTenantName()).eq(TenantEntity::getDelFlag, false);
        if (excludeId != null) {
            query.ne(TenantEntity::getId, excludeId);
        }
        if (baseMapper.selectCount(query) > 0) {
            throw new BusinessException("system.tenant.name.duplicated");
        }
    }

    private void apply(TenantAddDTO dto, TenantEntity entity) {
        entity.setTenantName(dto.getTenantName());
        entity.setDatasourceId(dto.getDatasourceId());
        entity.setOwnerType(dto.getOwnerType());
        entity.setIsolationMode(dto.getIsolationMode());
        entity.setExpireTime(dto.getExpireTime());
        entity.setApplicationCode(dto.getApplicationCode() == null ? null : JSON.toJSONString(dto.getApplicationCode()));
        entity.setRemark(dto.getRemark());
    }

    private TenantVO toVO(TenantEntity entity) {
        TenantVO vo = new TenantVO();
        copy(entity, vo);
        return vo;
    }

    private void copy(TenantEntity entity, TenantVO vo) {
        vo.setId(entity.getId());
        vo.setTenantName(entity.getTenantName());
        vo.setOwnerType(entity.getOwnerType());
        vo.setIsolationMode(entity.getIsolationMode());
        vo.setDatasourceId(entity.getDatasourceId());
        vo.setDatabaseName(entity.getDatabaseName());
        vo.setApplicationCode(entity.getApplicationCode() == null ? Collections.emptyList()
                : JSON.parseArray(entity.getApplicationCode(), String.class));
        vo.setExpireTime(entity.getExpireTime());
        vo.setInitStatus(entity.getInitStatus());
        vo.setInactive(entity.getInactive());
        vo.setRemark(entity.getRemark());
        vo.setCreatedTime(entity.getCreatedTime());
    }

    private Map<Integer, String> datasourceNameMap() {
        Map<Integer, String> map = new HashMap<>();
        datasourceMapper.selectList(null).forEach(e -> map.put(e.getId(), e.getDatasourceName()));
        return map;
    }
}
