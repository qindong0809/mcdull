package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 登录信息 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
@Data
public class LoginInfoLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    private String dictName;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 模块菜单名称
     */
    @NotBlank(groups = {ValidGroup.Export.class})
    private String moduleMenuName;

}