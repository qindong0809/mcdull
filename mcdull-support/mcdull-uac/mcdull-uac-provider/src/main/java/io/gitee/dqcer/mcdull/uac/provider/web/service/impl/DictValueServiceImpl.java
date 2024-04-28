package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictValueEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictValueVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDictValueRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDictValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dqcer
 */
@Service
public class DictValueServiceImpl
        extends BasicServiceImpl<IDictValueRepository> implements IDictValueService {


    @Override
    public PagedVO<DictValueVO> getList(DictValueQueryDTO dto) {
        Page<DictValueEntity> entityPage = baseRepository.selectPage(dto);
        List<DictValueVO> voList = new ArrayList<>();
        entityPage.getRecords().forEach(entity -> {
            DictValueVO vo = new DictValueVO();
            vo.setDictValueId(entity.getId());
            vo.setDictKeyId(entity.getDictKeyId());
            vo.setValueCode(entity.getValueCode());
            vo.setValueName(entity.getValueName());
            vo.setSort(entity.getSort());
            vo.setRemark(entity.getRemark());
            voList.add(vo);
        });
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(DictValueAddDTO dto) {
        List<DictValueEntity> entityList = baseRepository.getListByDictKeyId(dto.getDictKeyId());
        if (CollUtil.isNotEmpty(entityList)) {
            boolean anyMatch = entityList.stream().anyMatch(i -> i.getValueName().equals(dto.getValueName())
                    || i.getValueCode().equals(dto.getValueCode()));
            if (anyMatch) {
                LogHelp.error(log, "Data exists, {}-{}", dto.getValueName(), dto.getValueCode());
                throw new BusinessException(I18nConstants.DATA_EXISTS);
            }
        }
        baseRepository.insert(dto);
    }
}
