package io.gitee.dqcer.mcdull.system.provider.model.vo.administrator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AdminDeptListTreeVO implements VO {

    @DynamicDateFormat(enableTimezone = true, showTime = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date createTime;
    private Integer createUser;
    private String createUserString;
    private String description;
    private Integer id;
    private Boolean isSystem;
    private String name;
    private Integer parentId;
    private Integer sort;
    private Integer status;
    @DynamicDateFormat(enableTimezone = true, showTime = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date updateTime;
    private Integer updateUser;
    private String updateUserString;
    private List<AdminDeptListTreeVO> children;
}
