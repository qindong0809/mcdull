package io.gitee.dqcer.mcdull.system.provider.web.def;

import cn.hutool.core.bean.BeanUtil;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.facade.service.UserApi;
import io.gitee.dqcer.mcdull.system.facade.model.dto.UserListDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.facade.model.dto.UserDTO;
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
public class UserApiDefImpl implements UserApi {

    @Resource
    private IUserService userService;

    @Override
    public Map<Integer, UserDTO> getEntityMap(List<Integer> userIdList) {
        Map<Integer, UserEntity> entityMap = userService.getEntityMap(userIdList);
        return entityMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> toDTO(e.getValue())
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
    public UserDTO get(Integer userId) {
        UserEntity entity = userService.get(userId);
        return entity != null ? toDTO(entity) : null;
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
    public PagedVO<UserDTO> listByPage(UserListDTO dto) {
        PagedVO<?> page = userService.listByPage(dto);
        PagedVO<UserDTO> result = new PagedVO<>();
        result.setTotal(page.getTotal());
        result.setPageSize(page.getPageSize());
        result.setPageNum(page.getPageNum());
        result.setTotalPage((int) page.getTotalPage());
        result.setList(BeanUtil.copyToList(page.getList(), UserDTO.class));
        return result;
    }

    private UserDTO toDTO(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setEmployeeId(entity.getId());
        dto.setLoginName(entity.getLoginName());
        dto.setActualName(entity.getActualName());
        dto.setGender(entity.getGender());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setDepartmentId(entity.getDepartmentId());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setInactive(entity.getInactive());
        return dto;
    }
}
