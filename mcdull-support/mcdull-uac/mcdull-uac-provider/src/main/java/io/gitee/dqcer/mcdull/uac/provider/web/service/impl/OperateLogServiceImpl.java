package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.OperateLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IOperateLogRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IOperateLogService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class OperateLogServiceImpl extends BasicServiceImpl<IOperateLogRepository> implements IOperateLogService {

    @Resource
    private IUserService userService;

    @Override
    public PagedVO<LoginLogVO> queryByPage(LoginLogQueryDTO dto) {
        Page<LoginLogEntity> entityPage = baseRepository.selectPage(dto);
        List<LoginLogVO> voList = new ArrayList<>();
        List<LoginLogEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
//            Set<Long> userIdSet = records.stream().map(LoginLogEntity::getUserId).collect(Collectors.toSet());
//            Map<Long, UserEntity> userMap = userService.getEntityMap(new ArrayList<>(userIdSet));
            for (LoginLogEntity entity : records) {
                LoginLogVO vo = this.convertToConfigVO(entity);
//                UserEntity user = userMap.get(Convert.toLong(vo.getUserId()));
//                if (ObjUtil.isNotNull(user)) {
//                    vo.setUserName(user.getActualName());
//                }
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void save(OperateLogEntity dto) {
        baseRepository.save(dto);
    }


    private LoginLogVO convertToConfigVO(LoginLogEntity entity) {
        LoginLogVO loginLogVO = new LoginLogVO();
        loginLogVO.setLoginLogId(entity.getId());
        loginLogVO.setLoginName(entity.getLoginName());
        loginLogVO.setLoginIp(entity.getLoginIp());
        loginLogVO.setLoginIpRegion(entity.getLoginIpRegion());
        loginLogVO.setUserAgent(entity.getUserAgent());
        loginLogVO.setRemark(entity.getRemark());
        loginLogVO.setLoginResult(entity.getLoginResult());
        loginLogVO.setCreateTime(LocalDateTimeUtil.of(entity.getCreatedTime()));
        return loginLogVO;
    }
}
