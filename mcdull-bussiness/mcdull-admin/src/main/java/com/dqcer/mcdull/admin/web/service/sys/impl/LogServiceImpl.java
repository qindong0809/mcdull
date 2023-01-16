package io.gitee.dqcer.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.framework.base.util.PageUtil;
import io.gitee.dqcer.framework.base.vo.PagedVO;
import io.gitee.dqcer.framework.base.wrapper.Result;
import io.gitee.dqcer.admin.framework.log.IOperationLog;
import io.gitee.dqcer.admin.model.convert.sys.LogConvert;
import io.gitee.dqcer.admin.model.dto.sys.LogLiteDTO;
import io.gitee.dqcer.admin.model.entity.sys.LogDO;
import io.gitee.dqcer.admin.model.vo.sys.LogVO;
import io.gitee.dqcer.admin.web.dao.repository.sys.ILogRepository;
import io.gitee.dqcer.admin.web.service.sys.ILogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志服务 实现类
 *
 * @author dqcer
 * @version 2023/01/15 16:01:06
 */
@Service
public class LogServiceImpl implements ILogService, IOperationLog {

    @Resource
    private ILogRepository logRepository;

    /**
     * 保存
     *
     * @param dto dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(LogDO dto) {
        logRepository.save(dto);
    }


    /**
     * 列表页
     *
     * @param dto dto
     * @return {@link Result < PagedVO >}
     */
    @Override
    public Result<PagedVO<LogVO>> listByPage(LogLiteDTO dto) {
        Page<LogDO> entityPage = logRepository.selectPage(dto);
        List<LogVO> voList = new ArrayList<>();
        for (LogDO record : entityPage.getRecords()) {
            voList.add(LogConvert.convertToLogVO(record));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }


}
