package io.gitee.dqcer.mcdull.system.provider.model.vo;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.gitee.dqcer.mcdull.system.provider.model.bo.UserBO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 登录视图对象
 *
 * @author dqcer
 * @since 2024/03/09
 */
@Getter
@Setter
@ToString
public class LogonVO extends UserBO implements VO {

    @Schema(description = "token")
    private String token;

    @Schema(description = "菜单列表")
    private List<MenuVO> menuList;

    @Schema(description = "上次登录ip")
    private String lastLoginIp;

    @Schema(description = "上次登录ip地区")
    private String lastLoginIpRegion;

    @Schema(description = "上次登录user-agent")
    private String lastLoginUserAgent;

    @Schema(description = "上次登录时间")
    @DynamicDateFormat(dateFormat = DatePattern.NORM_DATETIME_PATTERN)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date lastLoginTime;
}
