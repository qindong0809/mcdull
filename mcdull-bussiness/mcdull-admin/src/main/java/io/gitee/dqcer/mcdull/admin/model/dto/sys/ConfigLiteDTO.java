package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 系统配置 接收客户端参数
 *
 * @author dqcer
 * @since 2022-11-16
 */
@Data
public class ConfigLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 参数名称
     */
    @Length(groups = {ValidGroup.Paged.class}, max = 128)
    private String name;

    /**
     * 参数键名
     */
    @Length(groups = {ValidGroup.Paged.class}, max = 128)
    private String configKey;

    /**
     * 参数键值
     */
    @Length(groups = {ValidGroup.Paged.class}, max = 128)
    private String value;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

}