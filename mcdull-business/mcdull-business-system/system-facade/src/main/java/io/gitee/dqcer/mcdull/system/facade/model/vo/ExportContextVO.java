package io.gitee.dqcer.mcdull.system.facade.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * 导出上下文（导出前一次性从 system 侧取回的环境参数）
 */
@Data
public class ExportContextVO implements VO {

    /**
     * 当前操作人名称（取用户表）
     */
    private String operatorName;

    /**
     * 当前菜单名称（取菜单表，用于拼导出文件名）
     */
    private String menuName;

    /**
     * 是否自动归档导出文件
     */
    private Boolean autoStorage;

    /**
     * 文件访问域名
     */
    private String fileDomainName;

    /**
     * 按用户时区格式化后的当前时间
     */
    private String dateTimeStr;
}
