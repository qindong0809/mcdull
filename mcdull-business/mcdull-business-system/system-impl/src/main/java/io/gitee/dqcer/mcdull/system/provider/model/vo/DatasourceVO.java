package io.gitee.dqcer.mcdull.system.provider.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DatasourceVO {
    private Integer id;
    private String datasourceName;
    private String jdbcUrl;
    private String username;
    private Boolean inactive;
    private String remark;
    private Integer referenceCount;
    private Date createdTime;
}
