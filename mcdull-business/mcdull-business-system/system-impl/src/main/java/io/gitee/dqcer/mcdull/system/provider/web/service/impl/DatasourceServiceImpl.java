package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.mysql.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceTestDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DatasourceEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TenantEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceOptionVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceTestVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceVO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.DatasourceConnectionTester;
import io.gitee.dqcer.mcdull.system.provider.web.manager.DatasourceCryptoManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.TenantCacheEvictManager;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.DatasourceMapper;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.TenantMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDatasourceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatasourceServiceImpl extends BasicCurdServiceImpl<DatasourceMapper, DatasourceEntity> implements IDatasourceService {

    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private DatasourceCryptoManager cryptoManager;
    @Resource
    private DatasourceConnectionTester connectionTester;
    @Resource
    private TenantCacheEvictManager cacheEvictManager;

    @Override
    public PagedVO<DatasourceVO> queryPage(DatasourceQueryDTO dto) {
        LambdaQueryWrapper<DatasourceEntity> query = new LambdaQueryWrapper<>();
        if (dto.getDatasourceName() != null && !dto.getDatasourceName().isEmpty()) {
            query.like(DatasourceEntity::getDatasourceName, dto.getDatasourceName());
        }
        if (dto.getInactive() != null) {
            query.eq(DatasourceEntity::getInactive, dto.getInactive());
        }
        query.orderByDesc(DatasourceEntity::getId);
        Page<DatasourceEntity> page = baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
        List<DatasourceVO> list = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return PageUtil.toPage(list, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DatasourceAddDTO dto) {
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new BusinessException("system.datasource.password.required");
        }
        DatasourceEntity entity = new DatasourceEntity();
        entity.setDatasourceName(dto.getDatasourceName());
        entity.setJdbcUrl(dto.getJdbcUrl());
        entity.setUsername(dto.getUsername());
        entity.setPasswordCipher(cryptoManager.encrypt(dto.getPassword()));
        entity.setRemark(dto.getRemark());
        baseMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DatasourceUpdateDTO dto) {
        DatasourceEntity entity = mustGet(dto.getId());
        entity.setDatasourceName(dto.getDatasourceName());
        entity.setJdbcUrl(dto.getJdbcUrl());
        entity.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPasswordCipher(cryptoManager.encrypt(dto.getPassword()));
        }
        entity.setRemark(dto.getRemark());
        baseMapper.updateById(entity);
        LambdaQueryWrapper<TenantEntity> query = new LambdaQueryWrapper<>();
        query.eq(TenantEntity::getDatasourceId, dto.getId()).eq(TenantEntity::getDelFlag, false);
        tenantMapper.selectList(query).forEach(t -> cacheEvictManager.clearTenant(t.getId(), null));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Integer id, Boolean inactive) {
        DatasourceEntity entity = mustGet(id);
        entity.setInactive(inactive);
        baseMapper.updateById(entity);
        LambdaQueryWrapper<TenantEntity> query = new LambdaQueryWrapper<>();
        query.eq(TenantEntity::getDatasourceId, id).eq(TenantEntity::getDelFlag, false);
        tenantMapper.selectList(query).forEach(t -> cacheEvictManager.clearTenant(t.getId(), null));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        LambdaQueryWrapper<TenantEntity> query = new LambdaQueryWrapper<>();
        query.eq(TenantEntity::getDatasourceId, id).eq(TenantEntity::getDelFlag, false);
        long count = tenantMapper.selectCount(query);
        if (count > 0) {
            throw new BusinessException("system.datasource.referenced");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<DatasourceOptionVO> options() {
        LambdaQueryWrapper<DatasourceEntity> query = new LambdaQueryWrapper<>();
        query.eq(DatasourceEntity::getInactive, false).eq(DatasourceEntity::getDelFlag, false);
        return baseMapper.selectList(query).stream().map(entity -> {
            DatasourceOptionVO vo = new DatasourceOptionVO();
            vo.setId(entity.getId());
            vo.setDatasourceName(entity.getDatasourceName());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public DatasourceTestVO test(DatasourceTestDTO dto) {
        String password = dto.getPassword();
        if ((password == null || password.isEmpty()) && dto.getId() != null) {
            password = cryptoManager.decrypt(mustGet(dto.getId()).getPasswordCipher());
        }
        if (password == null || password.isEmpty()) {
            throw new BusinessException("system.datasource.password.required");
        }
        return connectionTester.test(dto.getJdbcUrl(), dto.getUsername(), password);
    }

    @Override
    public DatasourceEntity getEntity(Integer id) {
        return baseMapper.selectById(id);
    }

    private DatasourceVO toVO(DatasourceEntity entity) {
        DatasourceVO vo = new DatasourceVO();
        vo.setId(entity.getId());
        vo.setDatasourceName(entity.getDatasourceName());
        vo.setJdbcUrl(maskUrl(entity.getJdbcUrl()));
        vo.setUsername(entity.getUsername());
        vo.setInactive(entity.getInactive());
        vo.setRemark(entity.getRemark());
        vo.setCreatedTime(entity.getCreatedTime());
        LambdaQueryWrapper<TenantEntity> query = new LambdaQueryWrapper<>();
        query.eq(TenantEntity::getDatasourceId, entity.getId()).eq(TenantEntity::getDelFlag, false);
        vo.setReferenceCount(Math.toIntExact(tenantMapper.selectCount(query)));
        return vo;
    }

    private String maskUrl(String url) {
        if (url == null) {
            return null;
        }
        return url.replaceAll("(password|pwd)=[^&]+", "$1=***");
    }
}
