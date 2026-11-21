package io.gitee.dqcer.mcdull.system.facade.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import java.util.List;

/**
 * 审批人部门回填查询入参
 */
@Data
public class ApproveDeptQueryDTO implements DTO {

    /**
     * 当前用户 id
     */
    private Integer userId;

    /**
     * 审批人用户 id 列表
     */
    private List<Integer> responsibleUserIdList;
}
