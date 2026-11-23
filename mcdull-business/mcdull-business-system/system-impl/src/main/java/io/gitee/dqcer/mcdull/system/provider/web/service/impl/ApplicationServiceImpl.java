package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.system.provider.model.enums.ApplicationEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ApplicationVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IApplicationService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 应用 Service 实现
 *
 * @author dqcer
 * @since 2026/11/23
 */
@Service
public class ApplicationServiceImpl implements IApplicationService {

    @Override
    public List<ApplicationVO> listVisible() {
        List<String> queriedPermissions = StpUtil.getPermissionList();
        List<String> permissionList = CollUtil.isEmpty(queriedPermissions)
                ? Collections.emptyList() : queriedPermissions;
        boolean administrator = UserContextHolder.isAdmin()
                || CollUtil.contains(permissionList, GlobalConstant.ALL_CODE);
        return Arrays.stream(ApplicationEnum.values())
                .filter(application -> administrator || permissionList.stream()
                        .anyMatch(permission -> permission.startsWith(application.getPermissionPrefix())))
                .map(application -> new ApplicationVO(application.getCode(), application.getText()))
                .toList();
    }
}
