package io.gitee.dqcer.mcdull.system.provider.model.vo;

import lombok.Data;

@Data
public class DatasourceTestVO {
    private Boolean success;
    private String message;
    private Long latencyMs;
    private String databaseProduct;
    private String databaseVersion;
}
