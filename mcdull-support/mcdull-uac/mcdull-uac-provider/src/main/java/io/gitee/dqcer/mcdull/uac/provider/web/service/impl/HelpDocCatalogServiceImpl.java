package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocCatalogAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.HelpDocCatalogUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.TableColumnUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocCatalogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.TableColumnEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.HelpDocCatalogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocCatalogRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ITableColumnRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IHelpDocCatalogService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ITableColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Help Doc Catalog Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class HelpDocCatalogServiceImpl
        extends BasicServiceImpl<IHelpDocCatalogRepository> implements IHelpDocCatalogService {


    @Override
    public List<HelpDocCatalogVO> getAll() {
        List<HelpDocCatalogEntity> list = baseRepository.list();
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
        List<HelpDocCatalogEntity> list = baseRepository.list(dto.getParentId());
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(null, dto.getName(), list,
                    entity -> entity.getName().equals(dto.getName()));
        }
        HelpDocCatalogEntity entity = new HelpDocCatalogEntity();
        entity.setName(dto.getName());
        entity.setSort(dto.getSort());
        entity.setParentId(dto.getParentId());
        baseRepository.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(HelpDocCatalogUpdateDTO dto) {
        Integer helpDocCatalogId = dto.getHelpDocCatalogId();
        HelpDocCatalogEntity helpDocCatalog = baseRepository.getById(helpDocCatalogId);
        if (ObjUtil.isNull(helpDocCatalog)) {
            this.throwDataNotExistException(helpDocCatalogId);
        }
        List<HelpDocCatalogEntity> list = baseRepository.list(dto.getParentId());
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(helpDocCatalogId, dto.getName(), list,
                    entity -> (!helpDocCatalogId.equals(entity.getId())) && entity.getName().equals(dto.getName()));
        }
        helpDocCatalog.setName(dto.getName());
        helpDocCatalog.setSort(dto.getSort());
        baseRepository.updateById(helpDocCatalog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer helpDocCatalogId) {
        HelpDocCatalogEntity helpDocCatalog = baseRepository.getById(helpDocCatalogId);
        if (ObjUtil.isNull(helpDocCatalog)) {
            this.throwDataNotExistException(helpDocCatalogId);
        }
        baseRepository.removeById(helpDocCatalog);
    }

    private HelpDocCatalogVO convert(HelpDocCatalogEntity entity) {
        HelpDocCatalogVO helpDocCatalogVO = new HelpDocCatalogVO();
        helpDocCatalogVO.setHelpDocCatalogId(Convert.toInt(entity.getId()));
        helpDocCatalogVO.setName(entity.getName());
        helpDocCatalogVO.setParentId(Convert.toInt(entity.getParentId()));
        helpDocCatalogVO.setSort(entity.getSort());
        return helpDocCatalogVO;
    }
}
