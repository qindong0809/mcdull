package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.audit.HelpDocCatalogAudit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.HelpDocCatalogAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.HelpDocCatalogUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.HelpDocCatalogEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.HelpDocCatalogVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.HelpDocCatalogMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IHelpDocCatalogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Help Doc Catalog Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class HelpDocCatalogServiceImpl
        extends BasicCurdServiceImpl<HelpDocCatalogMapper, HelpDocCatalogEntity> implements IHelpDocCatalogService {

    @Resource
    private IAuditManager auditManager;

    @Override
    public List<HelpDocCatalogVO> getAll() {
        List<HelpDocCatalogEntity> list = super.list();
        if (CollUtil.isNotEmpty(list)) {
            List<HelpDocCatalogVO> voList = new ArrayList<>();
            for (HelpDocCatalogEntity entity : list) {
                voList.add(this.convert(entity));
            }
            return voList;
        }
        return Collections.emptyList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(HelpDocCatalogAddDTO dto) {
        List<HelpDocCatalogEntity> list = this.list(dto.getParentId());
        if (CollUtil.isNotEmpty(list)) {
            LogicCheckUtil.validNameExist(null, dto.getName(), list,
                    entity -> entity.getName().equals(dto.getName()));
        }
        HelpDocCatalogEntity entity = new HelpDocCatalogEntity();
        entity.setName(dto.getName());
        entity.setSort(dto.getSort());
        entity.setParentId(dto.getParentId());
        baseMapper.insert(entity);

        auditManager.saveByAddEnum(dto.getName(), entity.getId(), this.buildAuditLog(entity));
    }

    private Audit buildAuditLog(HelpDocCatalogEntity entity) {
        HelpDocCatalogAudit audit = new HelpDocCatalogAudit();
        audit.setName(entity.getName());
        Integer parentId = entity.getParentId();
        if (ObjUtil.isNotNull(parentId)) {
            HelpDocCatalogEntity parentEntity = super.getById(parentId);
            if (ObjUtil.isNotNull(parentEntity)) {
                audit.setParentName(parentEntity.getName());
            }
        }
        audit.setSort(entity.getSort());
        return audit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(HelpDocCatalogUpdateDTO dto) {
        Integer helpDocCatalogId = dto.getHelpDocCatalogId();
        HelpDocCatalogEntity helpDocCatalog = super.mustGet(helpDocCatalogId);
        HelpDocCatalogEntity oldEntity = ObjUtil.cloneByStream(helpDocCatalog);
        List<HelpDocCatalogEntity> list = this.list(dto.getParentId());
        if (CollUtil.isNotEmpty(list)) {
            LogicCheckUtil.validNameExist(helpDocCatalogId, dto.getName(), list,
                    entity -> (!helpDocCatalogId.equals(entity.getId())) && entity.getName().equals(dto.getName()));
        }
        helpDocCatalog.setName(dto.getName());
        helpDocCatalog.setSort(dto.getSort());
        super.updateById(helpDocCatalog);
        auditManager.saveByUpdateEnum(dto.getName(), helpDocCatalogId,
                this.buildAuditLog(oldEntity), this.buildAuditLog(helpDocCatalog));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        HelpDocCatalogEntity helpDocCatalog = super.mustGet(id);
        super.removeById(helpDocCatalog);
        auditManager.saveByDeleteEnum(helpDocCatalog.getName(), id, null);
    }

    @Override
    public HelpDocCatalogEntity getById(Integer id) {
        return super.getById(id);
    }

    @Override
    public List<HelpDocCatalogEntity> queryListByIds(List<Integer> idList) {
        if (CollUtil.isNotEmpty(idList)) {
            return super.listByIds(idList);
        }
        return Collections.emptyList();
    }

    private HelpDocCatalogVO convert(HelpDocCatalogEntity entity) {
        HelpDocCatalogVO helpDocCatalogVO = new HelpDocCatalogVO();
        helpDocCatalogVO.setHelpDocCatalogId(Convert.toInt(entity.getId()));
        helpDocCatalogVO.setName(entity.getName());
        helpDocCatalogVO.setParentId(Convert.toInt(entity.getParentId()));
        helpDocCatalogVO.setSort(entity.getSort());
        return helpDocCatalogVO;
    }


    public Page<HelpDocCatalogEntity> selectPage(ConfigQueryDTO param) {
        LambdaQueryWrapper<HelpDocCatalogEntity> lambda = new QueryWrapper<HelpDocCatalogEntity>().lambda();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            lambda.like(HelpDocCatalogEntity::getName, keyword);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }



    public Integer insert(HelpDocCatalogEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    public boolean exist(HelpDocCatalogEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }


    public List<HelpDocCatalogEntity> list(Integer parentId) {
        if (ObjUtil.isNotNull(parentId)) {
            LambdaQueryWrapper<HelpDocCatalogEntity> query = Wrappers.lambdaQuery();
            query.eq(HelpDocCatalogEntity::getParentId, parentId);
            return baseMapper.selectList(query);
        }
        return Collections.emptyList();
    }

    /**
     * 根据id删除批处理
     *
     * @param ids id集
     */
    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}
