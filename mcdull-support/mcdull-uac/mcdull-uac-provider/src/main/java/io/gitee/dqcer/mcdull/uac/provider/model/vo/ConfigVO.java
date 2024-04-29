package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
* 系统配置 返回客户端值
*
* @author dqcer
* @since 2024-04-29
*/
@EqualsAndHashCode(callSuper = false)
@Data
public class ConfigVO implements VO {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long configId;

    /**
     * 参数名字
     */
    private String configName;

    /**
     * 参数key
     */
    private String configKey;

    private String configValue;

    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "上次修改时间")
    private LocalDateTime updateTime;


}