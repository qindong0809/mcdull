package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictKeyEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictKeyVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDictKeyRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDictKeyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author dqcer
 */
@Service
public class DictKeyServiceImpl
        extends BasicServiceImpl<IDictKeyRepository>  implements IDictKeyService {


    @Transactional(readOnly = true)
    @Override
    public List<DictKeyVO> queryAll() {
        List<DictKeyEntity> entityList = baseRepository.getListAll();
        if (CollUtil.isNotEmpty(entityList)) {
            return entityList.stream().map(entity -> {
                DictKeyVO vo = new DictKeyVO();
                vo.setDictKeyId(entity.getId());
                vo.setKeyCode(entity.getKeyCode());
                vo.setKeyName(entity.getKeyName());
                vo.setRemark(entity.getRemark());
                return vo;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    @Override
    public PagedVO<DictKeyVO> getList(DictKeyQueryDTO dto) {
        Page<DictKeyEntity> entityPage = baseRepository.selectPage(dto);
        List<DictKeyVO> voList = new ArrayList<>();
        entityPage.getRecords().forEach(entity -> {
            DictKeyVO vo = new DictKeyVO();
            vo.setDictKeyId(entity.getId());
            vo.setKeyCode(entity.getKeyCode());
            vo.setKeyName(entity.getKeyName());
            vo.setRemark(entity.getRemark());
            voList.add(vo);
        });
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(DictKeyAddDTO dto) {
        List<DictKeyEntity> listAll = baseRepository.getListAll();
        if (CollUtil.isNotEmpty(listAll)) {
            for (DictKeyEntity entity : listAll) {
                if (entity.getKeyCode().equals(dto.getKeyCode())
                        || entity.getKeyName().equals(dto.getKeyName())) {
                    throw new RuntimeException(I18nConstants.DATA_EXISTS);
                }
            }
        }
        baseRepository.insert(dto.getKeyCode(), dto.getKeyName(), dto.getRemark());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Long> idList) {
        List<DictKeyEntity> listAll = baseRepository.getListAll();
        if (CollUtil.isNotEmpty(listAll)) {
            List<Long> dbIdList = listAll.stream().map(IdEntity::getId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(dbIdList)) {
                for (Long id : idList) {
                    if (!dbIdList.contains(id)) {
                        throw new RuntimeException(I18nConstants.DATA_NOT_EXIST);
                    }
                }
            }
            baseRepository.removeBatchByIds(idList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(DictKeyUpdateDTO dto) {
        Long dictKeyId = dto.getDictKeyId();
        List<DictKeyEntity> listAll = baseRepository.getListAll();
        if (CollUtil.isNotEmpty(listAll)) {
            List<Long> dbIdList = listAll.stream().map(IdEntity::getId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(dbIdList)) {
                if (!dbIdList.contains(dictKeyId)) {
                    throw new RuntimeException(I18nConstants.DATA_NOT_EXIST);
                }
            }
            Map<Long, DictKeyEntity> map = listAll.stream().collect(Collectors
                    .toMap(IdEntity::getId, Function.identity()));
            map.forEach((k, v) -> {
                if (!dictKeyId.equals(k)) {
                    if (v.getKeyCode().equals(dto.getKeyCode()) || v.getKeyName().equals(dto.getKeyName())) {
                        throw new RuntimeException(I18nConstants.DATA_EXISTS);
                    }
                }
            });
            baseRepository.update(dictKeyId, dto.getKeyCode(), dto.getKeyName(), dto.getRemark());
        }
    }

    @Override
    public DictKeyVO  getByCode(String keyCode) {
        List<DictKeyEntity> listAll = baseRepository.getListAll();
        if (CollUtil.isNotEmpty(listAll)) {
            for (DictKeyEntity entity : listAll) {
                if (entity.getKeyCode().equals(keyCode)) {
                    DictKeyVO vo = new DictKeyVO();
                    vo.setDictKeyId(entity.getId());
                    vo.setKeyCode(entity.getKeyCode());
                    vo.setKeyName(entity.getKeyName());
                    vo.setRemark(entity.getRemark());
                    return vo;
                }
            }
        }
        return null;
    }
}
