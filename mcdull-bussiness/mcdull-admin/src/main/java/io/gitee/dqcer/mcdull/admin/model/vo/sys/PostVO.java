
package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.admin.framework.transformer.DictTransformer;
import io.gitee.dqcer.mcdull.admin.framework.transformer.UserTransformer;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.Date;

/**
 * 岗位 视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class PostVO implements VO {

    /**
     * 主键
     */
    private Long id;

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

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private Long createdBy;

    @Transform(from = "createdBy", transformer = UserTransformer.class)
    private String createdByStr;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 更新人
     */
    private Long updatedBy;

    private String updatedByStr;


    @Transform(from = "status", param = "sys_normal_disable", transformer = DictTransformer.class)
    private String statusStr;


    /**
     * 删除标识（1/正常 2/删除）
     */
    private Integer delFlag;

    /**
     * 删除str
     */
    @Transform(from = "delFlag", param = "del_flag_type", transformer = DictTransformer.class)
    private String delFlagStr;

}
