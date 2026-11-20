package io.gitee.dqcer.mcdull.demo.provider.model.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 演示任务业务对象：只在 impl 内部流转，不对外暴露。
 */
@Data
public class DemoTaskBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private String content;

    private Integer status;

    private Integer priority;

    private Date dueTime;

    private Date createdTime;

    private Date updatedTime;
}
