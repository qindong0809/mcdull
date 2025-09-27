package io.gitee.dqcer.mcdull.system.provider.model.vo.administrator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AdminUserVO implements VO {

    /** 主键 */
    private Integer id;

    /** 创建人中文名 */
    private String createUserString;

    /** 创建时间，格式：yyyy-MM-dd HH:mm:ss */
    @DynamicDateFormat(enableTimezone = true, showTime = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;

    /** 是否禁用 */
    private Boolean disabled;

    /** 更新人中文名 */
    private String updateUserString;

    /** 更新时间，格式：yyyy-MM-dd HH:mm:ss */
    @DynamicDateFormat(enableTimezone = true, showTime = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date updateTime;

    /** 登录账号 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 性别：1 男  2 女  0 未知 */
    private Integer gender;

    /** 头像 URL */
    private String avatar;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String phone;

    /** 状态：1 正常  2 停用 */
    private Integer status;

    /** 是否系统内置用户 */
    private Boolean isSystem;

    /** 个人描述/签名 */
    private String description;

    /** 部门 ID */
    private Integer deptId;

    /** 部门名称 */
    private String deptName;

    /** 角色 ID 列表 */
    private List<String> roleIds;

    /** 角色名称列表 */
    private List<String> roleNames;
}
