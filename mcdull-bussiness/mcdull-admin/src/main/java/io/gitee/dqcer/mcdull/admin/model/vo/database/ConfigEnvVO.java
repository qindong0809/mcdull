package io.gitee.dqcer.mcdull.admin.model.vo.database;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
*  返回客户端值
*
* @author dqcer
* @since 2023-08-29
*/
@Data
public class ConfigEnvVO implements VO {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 标识（1/dev 2/test 3/prod）
     */
    private Integer type;

    /**
     * 动态标题（1/正常 2/false）
     */
    private Boolean dynamicTitle;

    /**
     * 固定header（1/正常 2/false）
     */
    private Boolean fixedHeader;

    /**
     * 主题风格设置
     */
    private String sideTheme;

    /**
     * 显示logo（1/正常 2/false）
     */
    private Boolean sidebarLogo;

    /**
     * 开启Tags-Views（1/正常 2/false）
     */
    private Boolean tagsView;

    /**
     * 主题色
     */
    private String theme;

    /**
     * 开启TopNav（1/正常 2/false）
     */
    private Boolean topNav;


}