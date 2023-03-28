package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.ConfigConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.ConfigLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysConfigDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.ConfigVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.common.ISysConfigRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IConfigService;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class ConfigServiceImpl implements IConfigService {

    @Resource
    private ISysConfigRepository sysConfigRepository;

    @Override
    public Result<PagedVO<ConfigVO>> list(ConfigLiteDTO dto) {
        List<ConfigVO> voList = new ArrayList<>();
        Page<SysConfigDO> entityPage = sysConfigRepository.selectPage(dto);
        for (SysConfigDO entity : entityPage.getRecords()) {
            voList.add(ConfigConvert.convertToConfigVO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    @Override
    public Result<ConfigVO> detail(Long id) {
        SysConfigDO sysConfigDO = sysConfigRepository.getById(id);
        return Result.ok(ConfigConvert.convertToConfigVO(sysConfigDO));
    }
}
