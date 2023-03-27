package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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


}