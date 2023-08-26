package io.gitee.dqcer.mcdull.admin.model.dto.database;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*  接收客户端参数
*
* @author dqcer
* @since 2023-08-17
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class BackListDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String name;

    /**
     * 组号
     */
    private Long ticketId;

    /**
     * 备注
     */
    private String remark;

}