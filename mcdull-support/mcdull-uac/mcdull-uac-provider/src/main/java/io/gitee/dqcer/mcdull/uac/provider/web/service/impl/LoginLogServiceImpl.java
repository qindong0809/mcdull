package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginLogResultTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ILoginLogRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginLogService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Login Log Service Impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class LoginLogServiceImpl
        extends BasicServiceImpl<ILoginLogRepository> implements ILoginLogService {

    @Resource
    private IUserService userService;
    @Resource
    private ICommonManager commonManager;

    @Transactional(readOnly = true)
    @Override
    public PagedVO<LoginLogVO> queryByPage(LoginLogQueryDTO dto) {
        Integer userId = dto.getUserId();
        if (ObjUtil.isNotNull(userId)) {
            UserEntity user = userService.get(userId);
            if (ObjUtil.isNotNull(user)) {
                dto.setUserName(user.getLoginName());
            }
        }
        Page<LoginLogEntity> entityPage = baseRepository.selectPage(dto);
        List<LoginLogVO> voList = new ArrayList<>();
        List<LoginLogEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            for (LoginLogEntity entity : records) {
                LoginLogVO vo = this.convertToConfigVO(entity);
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void add(LoginLogEntity entity) {
        String remark = entity.getRemark();
        if (StrUtil.isNotBlank(remark)) {
            entity.setRemark(dynamicLocaleMessageSource.getMessage(remark));
        }
        baseRepository.insert(entity);
    }

    @Override
    public LoginLogEntity getFirstLoginLog(String loginName) {
        return baseRepository.getFirst(loginName);
    }

    @Override
    public LoginLogEntity getLastLoginLog(String loginName) {
        List<LoginLogEntity> list = baseRepository.getListByLoginName(loginName);
        if (CollUtil.isNotEmpty(list)) {
            List<LoginLogEntity> successLoginList = list.stream()
                    .filter(item -> LoginLogResultTypeEnum.LOGIN_SUCCESS.getCode().equals(item.getLoginResult()))
                    .sorted((o1, o2) -> o2.getCreatedTime().compareTo(o1.getCreatedTime())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(successLoginList)) {
                return successLoginList.get(0);
            }
        }
        return null;
    }

    @Override
    public void exportData(LoginLogQueryDTO dto) {
        commonManager.exportExcel(dto, this::queryByPage, "登录日志", this.getTitleMap(), this::convertMap);
    }

    private Map<String, String> convertMap(LoginLogVO loginLogVO) {
        Map<String, String> map = new HashMap<>();
        map.put("loginName", loginLogVO.getLoginName());
        map.put("loginIp", loginLogVO.getLoginIp());
        map.put("loginIpRegion", loginLogVO.getLoginIpRegion());
        map.put("userAgent", loginLogVO.getUserAgent());
        map.put("loginResultName", loginLogVO.getLoginResultName());
        map.put("remark", loginLogVO.getRemark());
        map.put("createTime", commonManager.convertDateTimeStr(loginLogVO.getCreateTime()));
        return map;
    }

    private Map<String, String> getTitleMap() {
        Map<String, String> titleMap = new HashMap<>(8);
        titleMap.put("登录名", "loginName");
        titleMap.put("IP", "loginIp");
        titleMap.put("IP地区", "loginIpRegion");
        titleMap.put("设备信息", "userAgent");
        titleMap.put("登录结果", "loginResultName");
        titleMap.put("备注", "remark");
        titleMap.put("时间", "createTime");
        return titleMap;
    }

    private LoginLogVO convertToConfigVO(LoginLogEntity entity) {
        LoginLogVO vo = new LoginLogVO();
        vo.setLoginLogId(entity.getId());
        vo.setUserId(entity.getId());
        vo.setLoginName(entity.getLoginName());
        vo.setLoginIp(entity.getLoginIp());
        vo.setLoginIpRegion(entity.getLoginIpRegion());
        vo.setUserAgent(entity.getUserAgent());
        vo.setRemark(entity.getRemark());
        vo.setLoginResult(entity.getLoginResult());
        vo.setLoginResultName(IEnum.getTextByCode(LoginLogResultTypeEnum.class, entity.getLoginResult()));
        vo.setCreateTime(entity.getCreatedTime());
        return vo;
    }
}
