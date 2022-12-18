package com.dqcer.mcdull.mdc.provider.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqcer.framework.base.util.PageUtil;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.FeignResultParse;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.provider.model.convert.LogConvert;
import com.dqcer.mcdull.mdc.provider.model.dto.LogLiteDTO;
import com.dqcer.mcdull.mdc.provider.model.dto.SysLogFeignDTO;
import com.dqcer.mcdull.mdc.provider.model.entity.LogDO;
import com.dqcer.mcdull.mdc.provider.model.vo.LogVO;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.ILogRepository;
import com.dqcer.mcdull.uac.client.service.UserClientService;
import com.google.common.collect.Lists;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    @Resource
    private ILogRepository logRepository;

    @Resource
    private UserClientService userClientService;

    @Resource
    private ThreadPoolTaskExecutor threadPool;

    public Result<Integer> batchSave(List<SysLogFeignDTO> dto) {
        threadPool.submit(() -> {
            List<LogDO> entities = Lists.newArrayList();
            entities.addAll(dto);
            logRepository.saveBatch(entities, entities.size());
        });
        return Result.ok(dto.size());
    }

    public Result<PagedVO<LogVO>> listByPage(LogLiteDTO dto) {
        Page<LogDO> entityPage = logRepository.selectPage(dto);
        List<LogVO> voList = new ArrayList<>();
        for (LogDO entity : entityPage.getRecords()) {
            LogVO logVO = LogConvert.entity2Vo(entity);
            String nickname = FeignResultParse.getInstance(userClientService.getDetail(logVO.getAccountId())).getNickname();
            logVO.setAccountIdStr(nickname);
            voList.add(logVO);
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }
}
