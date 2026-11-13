package io.gitee.dqcer.mcdull.system.provider.web.def;

import io.gitee.dqcer.mcdull.system.facade.service.def.RoleApiDef;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.system.facade.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色能力实现
 * <p>
 * {@code @Primary}：单体模式下优先注入本地实现。
 * {@code @RestController}：微服务模式下暴露为 HTTP 接口供 Feign 调用。
 */
@Primary
@RestController
public class RoleApiDefImpl implements RoleApiDef {

    @Resource
    private IRoleService roleService;

    @Override
    public List<RoleVO> all() {
        return roleService.all();
    }

    @Override
    public Map<Integer, List<RoleVO>> getRoleMap(List<Integer> userIdList) {
        Map<Integer, List<RoleEntity>> entityMap = roleService.getRoleMap(userIdList);
        return entityMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(this::toVO).collect(Collectors.toList())
                ));
    }

    private RoleVO toVO(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        RoleVO vo = new RoleVO();
        vo.setRoleId(entity.getId());
        vo.setRoleName(entity.getRoleName());
        vo.setRoleCode(entity.getRoleCode());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}
