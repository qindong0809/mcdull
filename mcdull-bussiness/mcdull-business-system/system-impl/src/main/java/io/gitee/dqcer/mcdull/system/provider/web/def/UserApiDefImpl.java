package io.gitee.dqcer.mcdull.system.provider.web.def;

import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.facade.service.def.UserApiDef;
import io.gitee.dqcer.mcdull.system.facade.model.dto.UserListDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.facade.model.vo.UserVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户能力实现
 * <p>
 * {@code @Primary}：单体模式下若同时存在本地实现与 Feign 代理，强制优先注入本地实现。
 * {@code @RestController}：微服务模式下自动把契约暴露为标准 HTTP 接口供 Feign 调用。
 */
@Primary
@RestController
public class UserApiDefImpl implements UserApiDef {

    @Resource
    private IUserService userService;

    @Override
    public Map<Integer, UserVO> getEntityMap(List<Integer> userIdList) {
        Map<Integer, UserEntity> entityMap = userService.getEntityMap(userIdList);
        return entityMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> toVO(e.getValue())
                ));
    }

    @Override
    public Map<Integer, String> getNameMap(List<Integer> userIdList) {
        return userService.getNameMap(userIdList);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<Integer, String> getMap(Collection<?> entityList) {
        // 契约不暴露 BaseEntity，实现层在这里做适配
        List list = new ArrayList();
        for (Object obj : entityList) {
            if (obj instanceof BaseEntity) {
                list.add(obj);
            }
        }
        return userService.getMap(list);
    }

    @Override
    public UserVO get(Integer userId) {
        UserEntity entity = userService.get(userId);
        return entity != null ? toVO(entity) : null;
    }

    @Override
    public List<Integer> getUserIdList(Integer departmentId) {
        return userService.getUserIdList(departmentId);
    }

    @Override
    public List<LabelValueVO<Integer, String>> getResponsibleList() {
        return userService.getResponsibleList();
    }

    @Override
    public PagedVO<UserVO> listByPage(UserListDTO dto) {
        return userService.listByPage(dto);
    }

    private UserVO toVO(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setEmployeeId(entity.getId());
        vo.setLoginName(entity.getLoginName());
        vo.setActualName(entity.getActualName());
        vo.setGender(entity.getGender());
        vo.setPhone(entity.getPhone());
        vo.setEmail(entity.getEmail());
        vo.setDepartmentId(entity.getDepartmentId());
        vo.setCreatedBy(entity.getCreatedBy());
        vo.setUpdatedBy(entity.getUpdatedBy());
        vo.setInactive(entity.getInactive());
        return vo;
    }
}
