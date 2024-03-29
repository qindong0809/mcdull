package io.gitee.dqcer.mcdull.admin.model.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 岗位信息 实体
 *
 * @author dqcer
 * @since 2022/12/18
 */
@TableName("sys_post")
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class PostEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 显示顺序
     */
    private Integer postSort;

    /**
     * 状态（1正常 2停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
