package io.gitee.dqcer.mcdull.blaze.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.blaze.domain.form.PermissionDTO;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;

import java.util.List;
import java.util.function.Function;

public class BlazeBasicController extends BasicController {

    @Resource
    private IUserManager userManager;
    @Resource
    private IUserService userService;

    protected <VO, DTO extends PermissionDTO> VO executeByPermission(String code, VO emptyDefaultValue, DTO dto, Function<DTO, VO> function) {
        if (StrUtil.isNotBlank(code)) {
            boolean permissionApprove = StpUtil.hasPermission(code);
            if (permissionApprove) {
                UserEntity userEntity = userService.get(UserContextHolder.userId());
                Integer departmentId = userEntity.getDepartmentId();
                List<Integer> userIdList = userManager.getUserIdList(departmentId);
                if (CollUtil.isEmpty(userIdList)) {
                    return emptyDefaultValue;
                }
                dto.setResponsibleUserIdList(userIdList);
                return function.apply(dto);
            }
        }
        dto.setResponsibleUserIdList(ListUtil.of(UserContextHolder.userId()));
        return function.apply(dto);
    }
}
