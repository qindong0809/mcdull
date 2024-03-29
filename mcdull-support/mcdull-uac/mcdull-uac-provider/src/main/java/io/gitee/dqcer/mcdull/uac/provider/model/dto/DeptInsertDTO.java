package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import java.util.Date;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
@Data
public class DeptInsertDTO implements DTO {

    private static final long serialVersionUID = 1L;


    private Integer status;

    private Date createTime;

    private String name;

    private Integer parentId;

    private Integer sort;

    private String phone;

    private Integer principal;

    private String email;

    private String type;

    private String remark;

   
}