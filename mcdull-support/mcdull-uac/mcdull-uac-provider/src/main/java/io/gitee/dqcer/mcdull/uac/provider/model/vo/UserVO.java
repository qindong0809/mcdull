package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 用户视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
@ToString
public class UserVO implements VO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Integer employeeId;

    private String loginName;

    private String loginPwd;

    private String actualName;

    private Integer gender;

    private String phone;

    private Integer departmentId;

    private String departmentName;

    @Schema(description = "角色列表")
    private List<Integer> roleIdList;

    @Schema(description = "角色名称列表")
    private List<String> roleNameList;

    private Boolean administratorFlag;

    private String remark;

    private Integer createdBy;

    private Integer updatedBy;

    protected LocalDateTime createdTime;

    protected LocalDateTime updatedTime;

}
