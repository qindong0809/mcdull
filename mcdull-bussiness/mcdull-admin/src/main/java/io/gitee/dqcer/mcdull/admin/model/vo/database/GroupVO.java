package io.gitee.dqcer.mcdull.admin.model.vo.database;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

import java.util.Date;

/**
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class GroupVO implements VO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String status;

    private Date createdTime;

}
