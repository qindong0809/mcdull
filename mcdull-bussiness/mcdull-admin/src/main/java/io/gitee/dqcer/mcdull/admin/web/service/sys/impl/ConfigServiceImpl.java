package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.ConfigConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.ConfigVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.common.ISysConfigRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IConfigService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class ConfigServiceImpl extends BasicServiceImpl<ISysConfigRepository> implements IConfigService {


    @Override
    public Result<PagedVO<ConfigVO>> list(ConfigLiteDTO dto) {
        List<ConfigVO> voList = new ArrayList<>();
        Page<SysConfigEntity> entityPage = baseRepository.selectPage(dto);
        for (SysConfigEntity entity : entityPage.getRecords()) {
            voList.add(ConfigConvert.convertToConfigVO(entity));
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }

    @Override
    public Result<ConfigVO> detail(Long id) {
        SysConfigEntity sysConfigDO = baseRepository.getById(id);
        return Result.success(ConfigConvert.convertToConfigVO(sysConfigDO));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(ConfigAddDTO dto) {
        List<SysConfigEntity> list = baseRepository.getListByKey(dto.getConfigKey());
        this.validNameExist(null, dto.getConfigKey(), list);
        SysConfigEntity sysConfigDO = ConfigConvert.convertTOConfigDo(dto);
        baseRepository.save(sysConfigDO);
        return Result.success(sysConfigDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> edit(ConfigEditDTO dto) {
        List<SysConfigEntity> list = baseRepository.getListByKey(dto.getConfigKey());
        this.validNameExist(dto.getId(), dto.getConfigKey(), list);
        SysConfigEntity sysConfigDO = ConfigConvert.convertTOConfigDo(dto);
        sysConfigDO.setId(dto.getId());
        baseRepository.updateById(sysConfigDO);
        return Result.success(sysConfigDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> remove(Long id) {
        baseRepository.removeUpdate(id);
        return Result.success(id);
    }
}
