package com.dqcer.mcdull.mdc.provider.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqcer.framework.base.feign.FeignResultParse;
import com.dqcer.framework.base.page.PageUtil;
import com.dqcer.framework.base.page.Paged;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.convert.LogConvert;
import com.dqcer.mcdull.mdc.api.dto.LogLiteDTO;
import com.dqcer.mcdull.mdc.api.dto.SysLogFeignDTO;
import com.dqcer.mcdull.mdc.api.entity.LogEntity;
import com.dqcer.mcdull.mdc.api.vo.LogVO;
import com.dqcer.mcdull.mdc.provider.web.dao.repository.ILogRepository;
import com.dqcer.mcdull.uac.client.service.UserClientService;
import com.google.common.collect.Lists;
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

    public Result<Integer> batchSave(List<SysLogFeignDTO> dto) {
        List<LogEntity> entities = Lists.newArrayList();
        for (SysLogFeignDTO sysLogFeignDTO : dto) {
            entities.add(sysLogFeignDTO);
        }
        logRepository.saveBatch(entities, entities.size());
        return Result.ok(entities.size());
    }

    public Result<Paged<LogVO>> listByPage(LogLiteDTO dto) {
        Page<LogEntity> entityPage = logRepository.selectPage(dto);
        List<LogVO> voList = new ArrayList<>();
        for (LogEntity entity : entityPage.getRecords()) {
            LogVO logVO = LogConvert.entity2Vo(entity);
            String nickname = FeignResultParse.getInstance(userClientService.getDetail(logVO.getAccountId())).getNickname();
            logVO.setAccountIdStr(nickname);
            voList.add(logVO);
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }
}
