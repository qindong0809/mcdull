package io.gitee.dqcer.mcdull.admin.model.vo.database;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

import java.util.Date;

/**
*  返回客户端值
*
* @author dqcer
* @since 2023-08-17
*/
@Data
public class BackListVO implements VO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Integer model;

    private Long bizId;

    private String remark;

    private Date createdTime;

    private String createdByStr;

    private Long createdBy;
}