package io.gitee.dqcer.mcdull.system.provider.model.vo.administrator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AdminRoleVO implements VO {

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

   private String name;

   private String code;

   private String description;

   private Boolean isSystem;

   private Integer dataScope;

   private Integer sort;

   private List<Integer> menuIds;
}
