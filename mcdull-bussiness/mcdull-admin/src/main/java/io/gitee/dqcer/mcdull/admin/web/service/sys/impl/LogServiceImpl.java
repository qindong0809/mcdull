package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.framework.log.IOperationLog;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.LogConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.LogDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.LogVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.ILogRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IMenuRepository;
import io.gitee.dqcer.mcdull.admin.web.service.sys.ILogService;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 日志服务 实现类
 *
 * @author dqcer
 * @since 2023/01/15 16:01:06
 */
@Service
public class LogServiceImpl extends BasicServiceImpl<ILogRepository> implements ILogService, IOperationLog {


    @Resource
    private IMenuRepository menuRepository;

    /**
     * 保存 也可存入MongoDB
     *
     * @param dto dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(LogDO dto) {
        baseRepository.save(dto);
    }


    /**
     * 列表页
     *
     * @param dto dto
     * @return {@link Result < PagedVO >}
     */
    @Transactional(readOnly = true)
    @Override
    public Result<PagedVO<LogVO>> listByPage(LogLiteDTO dto) {
        List<MenuDO> list = menuRepository.list();
        Map<String, MenuDO> map = list.stream().collect(Collectors.toMap(MenuDO::getPerms, Function.identity()));

        Map<Long, MenuDO> parentMenuMap = list.stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
        Page<LogDO> entityPage = baseRepository.selectPage(dto);
        List<LogVO> voList = new ArrayList<>();
        for (LogDO record : entityPage.getRecords()) {
            LogVO logVO = LogConvert.convertToLogVO(record);

            MenuDO menuDO = map.get(record.getButton());
            logVO.setButton(menuDO.getName());
            MenuDO menu = parentMenuMap.get(menuDO.getParentId());
            logVO.setMenu(menu.getName());

            voList.add(logVO);
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }


}
