package io.gitee.dqcer.mcdull.admin.model.vo.database;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

/**
*  返回客户端值
*
* @author dqcer
* @since 2023-08-17
*/
@Data
public class TicketVO implements VO {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 工单号
     */
    private String number;

    /**
     * 组号
     */
    private Long groupId;

    /**
     * 执行方式（1/停放执行 2/在线执行）
     */
    private Integer executeType;

    /**
     * 是否合入初始化脚本（1/是 2/否）
     */
    private Integer hasMerge;

    /**
     * 备注
     */
    private String remark;

    /**
     * sql 脚本
     */
    private String sqlScript;

    /**
     * 流程状态（1可编辑 2已发布 3已通过 4已执行）
     */
    private Integer followStatus;

    /**
     * 回退状态（1正常 2dev:撤回 3test:不通过 4prod:驳回）
     */
    private Integer cancelStatus;

    /**
     * 状态（1正常 2停用）
     */
    private String status;
}