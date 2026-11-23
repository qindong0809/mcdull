package io.gitee.dqcer.mcdull.blaze.model.dto;

import java.util.List;

public interface PermissionDTO {

    void setResponsibleUserIdList(List<Integer> responsibleUserIdList);

    List<Integer> getResponsibleUserIdList();
}
