package io.gitee.dqcer.mcdull.mdc.provider.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.FeignResultParse;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.provider.model.convert.LogConvert;
import io.gitee.dqcer.mcdull.mdc.provider.model.dto.LogLiteDTO;
import io.gitee.dqcer.mcdull.mdc.provider.model.dto.SysLogFeignDTO;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.LogDO;
import io.gitee.dqcer.mcdull.mdc.provider.model.vo.LogVO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.ILogRepository;
import io.gitee.dqcer.mcdull.uac.client.service.UserClientService;
import com.google.common.collect.Lists;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志服务
 *
 * @author dqcer
 * @version 2022/12/26
 */
@Service
public class LogService {

    @Resource
    private ILogRepository logRepository;

    @Resource
    private UserClientService userClientService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public Result<Integer> batchSave(List<SysLogFeignDTO> dto) {
        threadPoolTaskExecutor.submit(() -> {
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