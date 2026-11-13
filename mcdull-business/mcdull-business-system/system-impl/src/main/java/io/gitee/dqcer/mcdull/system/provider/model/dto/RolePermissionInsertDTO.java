package io.gitee.dqcer.mcdull.system.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import java.util.List;

@Data
public class RolePermissionInsertDTO implements DTO {

    private List<Integer> menuIdList;
}
