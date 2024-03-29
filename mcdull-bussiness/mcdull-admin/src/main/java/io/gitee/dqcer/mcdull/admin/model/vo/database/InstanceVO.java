package io.gitee.dqcer.mcdull.admin.model.vo.database;

import io.gitee.dqcer.mcdull.admin.framework.transformer.UserTransformer;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

import java.util.Date;

/**
 * @author dqcer
 * @since  2022/11/27
 */
@Data
public class InstanceVO implements VO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long groupId;

    private String name;

    private String groupName;

    private String host;

    private Integer port;

    private String databaseName;

    private String username;

    private String password;

    private String status;

    private Date createdTime;

    private Long createdBy;

    @Transform(from = "createdBy", transformer = UserTransformer.class)
    private String createdByStr;

    private Date updatedTime;

    private Long updatedBy;

    @Transform(from = "updatedBy", transformer = UserTransformer.class)
    private String updatedByStr;

}
