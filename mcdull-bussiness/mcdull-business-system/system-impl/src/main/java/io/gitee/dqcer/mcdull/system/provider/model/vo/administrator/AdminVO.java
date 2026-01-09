
package io.gitee.dqcer.mcdull.system.provider.model.vo.administrator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 登录用户响应参数
 *
 * @author Charles7c
 * @since 2022/12/29 20:15
 */
@Data
@Schema(description = "登录用户响应参数")
public class AdminVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(description = "ID", example = "1")
    private Integer id;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    /**
     * 性别
     */
    @Schema(description = "性别", example = "1")
    private Integer gender;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "c*******@126.com")
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码", example = "188****8888")
    private String phone;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址", example = "https://himg.bdimg.com/sys/portrait/item/public.1.81ac9a9e.rf1ix17UfughLQjNo7XQ_w.jpg")
    private String avatar;

    /**
     * 描述
     */
    @Schema(description = "描述", example = "张三描述信息")
    private String description;

    /**
     * 最后一次修改密码时间
     */
    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date pwdResetTime;

    /**
     * 密码是否已过期
     */
    @Schema(description = "密码是否已过期", example = "true")
    private Boolean pwdExpired;

    /**
     * 创建时间
     */
    @DynamicDateFormat(enableTimezone = true, showTime = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;

    /**
     * 注册日期
     */
    @DynamicDateFormat(enableTimezone = true, showTime = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date registrationDate;

    /**
     * 部门 ID
     */
    @Schema(description = "部门 ID", example = "1")
    private Integer deptId;

    /**
     * 所属部门
     */
    @Schema(description = "所属部门", example = "测试部")
    private String deptName;

    /**
     * 权限码集合
     */
    @Schema(description = "权限码集合", example = "[\"system:user:list\",\"system:user:add\"]")
    private Set<String> permissions;

    /**
     * 角色编码集合
     */
    @Schema(description = "角色编码集合", example = "[\"test\"]")
    private Set<String> roles;

    /**
     * 角色名称列表
     */
    @Schema(description = "角色名称列表", example = "测试人员")
    private List<String> roleNames;

}
