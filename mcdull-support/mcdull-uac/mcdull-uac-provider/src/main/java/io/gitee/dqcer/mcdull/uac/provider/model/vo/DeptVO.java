package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.Date;

/**
 * 角色视图对象
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class DeptVO implements VO {

    private Integer id;

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
