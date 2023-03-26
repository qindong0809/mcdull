package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
public class RoleLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 状态（1/正常 2/停用）
     */
     @EnumsStrValid(groups = {ValidGroup.List.class}, value = StatusEnum.class)
     private String status;

    /**
     * 名称
     */
    @Length(groups = {ValidGroup.List.class}, max = 512)
    private String name;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    public String getStatus() {
        return status;
    }

    public RoleLiteDTO setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getName() {
        return name;
    }

    public RoleLiteDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public RoleLiteDTO setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public RoleLiteDTO setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }
}