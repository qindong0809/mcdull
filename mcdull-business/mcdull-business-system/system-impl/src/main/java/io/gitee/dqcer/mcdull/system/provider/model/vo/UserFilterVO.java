package io.gitee.dqcer.mcdull.system.provider.model.vo;


import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;


/**
 * user filter
 *
 * @author dqcer
 * @since  2026/02/09
 */
@Data
public class UserFilterVO implements VO {

    private Integer id;
    private Integer userId;
    private Integer roleId;
    private String categoryCode;
    private String subCategoryCode;
    private String filterTitle;
    private String filterContent;

}
