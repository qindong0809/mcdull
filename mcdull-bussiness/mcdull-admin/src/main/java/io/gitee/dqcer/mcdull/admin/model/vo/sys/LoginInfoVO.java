package io.gitee.dqcer.mcdull.admin.model.vo.sys;

import io.gitee.dqcer.mcdull.admin.framework.transformer.UserTransformer;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.vo.VO;
import lombok.Data;

import java.util.Date;

/**
* 登录日志记录 返回客户端值
*
* @author dqcer
* @since 2023-01-14
*/
@Data
public class LoginInfoVO implements VO {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    @Transform(from = "userId", transformer = UserTransformer.class)
    private String userName;

    /**
     * token
     */
    private String token;


    /**
     * 类型（1/登录 2/注销）
     */
    private Integer type;
}