package io.gitee.dqcer.mcdull.admin.model.vo.database;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

/**
*  返回客户端值
*
* @author dqcer
* @since 2023-08-17
*/
@Data
public class BackVO implements VO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long ticketId;

    private String name;

    private String remark;

    private String fileNameList;

}