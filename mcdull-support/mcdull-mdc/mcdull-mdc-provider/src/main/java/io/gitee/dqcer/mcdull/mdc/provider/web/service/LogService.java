package io.gitee.dqcer.mcdull.mdc.provider.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.provider.model.convert.LogConvert;
import io.gitee.dqcer.mcdull.mdc.provider.model.dto.LogLiteDTO;
import io.gitee.dqcer.mcdull.mdc.provider.model.dto.SysLogFeignDTO;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.LogEntity;
import io.gitee.dqcer.mcdull.mdc.provider.model.vo.LogVO;
import io.gitee.dqcer.mcdull.system.client.service.UserAip;
import jakarta.annotation.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志服务
 *
 * @author dqcer
 * @since 2022/12/26
 */
@Service
public class LogService {

//    @Resource
//    private ILogRepository logRepository;

    @Resource
    private UserAip userClientService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public Result<Integer> batchSave(List<SysLogFeignDTO> dto) {
        threadPoolTaskExecutor.submit(() -> {
            List<LogEntity> entities = new ArrayList<>();
            entities.addAll(dto);
//            logRepository.saveBatch(entities, entities.size());
        });
        return Result.success(dto.size());
    }

    public Result<PagedVO<LogVO>> listByPage(LogLiteDTO dto) {
        Page<LogEntity> entityPage = null;
        List<LogVO> voList = new ArrayList<>();
        for (LogEntity entity : entityPage.getRecords()) {
            LogVO logVO = LogConvert.entity2Vo(entity);
//            String nickname = ResultApiParse.getInstance(userClientService.getDetail(logVO.getAccountId())).getNickname();
//            logVO.setAccountIdStr(nickname);
            voList.add(logVO);
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }
}
