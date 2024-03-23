package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

import java.util.Date;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class RoleVO implements VO {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private Integer createdBy;

    private String createdByStr;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 更新人
     */
    private Integer updatedBy;

    private String updatedByStr;


    /**
     * 账户
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     *  类型（1/自定义 2/内置）
     */
    private Integer type;

    private Boolean inactive;

}
