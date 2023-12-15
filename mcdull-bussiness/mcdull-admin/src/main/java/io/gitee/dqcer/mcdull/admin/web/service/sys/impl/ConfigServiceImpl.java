package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.ConfigConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.ConfigVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.common.ISysConfigRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IConfigService;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
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
        Page<SysConfigDO> entityPage = baseRepository.selectPage(dto);
        for (SysConfigDO entity : entityPage.getRecords()) {
            voList.add(ConfigConvert.convertToConfigVO(entity));
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }

    @Override
    public Result<ConfigVO> detail(Long id) {
        SysConfigDO sysConfigDO = baseRepository.getById(id);
        return Result.success(ConfigConvert.convertToConfigVO(sysConfigDO));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(ConfigAddDTO dto) {
        List<SysConfigDO> list = baseRepository.getListByKey(dto.getConfigKey());
        this.validNameExist(null, dto.getConfigKey(), list);
        SysConfigDO sysConfigDO = ConfigConvert.convertTOConfigDo(dto);
        sysConfigDO.setDelFlag(DelFlayEnum.NORMAL.getCode());
        baseRepository.save(sysConfigDO);
        return Result.success(sysConfigDO.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> edit(ConfigEditDTO dto) {
        List<SysConfigDO> list = baseRepository.getListByKey(dto.getConfigKey());
        this.validNameExist(dto.getId(), dto.getConfigKey(), list);
        SysConfigDO sysConfigDO = ConfigConvert.convertTOConfigDo(dto);
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
