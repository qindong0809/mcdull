package com.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqcer.framework.base.util.PageUtil;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.admin.model.convert.sys.LogConvert;
import com.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import com.dqcer.mcdull.admin.model.entity.sys.LogDO;
import com.dqcer.mcdull.admin.model.vo.sys.LogVO;
import com.dqcer.mcdull.admin.web.dao.repository.sys.ILogRepository;
import com.dqcer.mcdull.admin.web.service.sys.ILogService;
import com.dqcer.mcdull.framework.mysql.config.DataChangeRecorderInnerInterceptor;
import com.dqcer.mcdull.framework.mysql.config.IDataChangeRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogServiceImpl implements ILogService {

    private static final Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

    @Resource
    private ILogRepository logRepository;


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
