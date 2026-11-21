package io.gitee.dqcer.mcdull.workflow.facade.constants;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;

/**
 * workflow 契约常量。
 * <p>
 * 服务名与路径是服务双方必须一致的线上契约，不能改成配置项。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public final class WorkflowConstant {

    /**
     * 注册中心服务名，取自 workflow-bootstrap 的 spring.application.name。
     */
    public static final String SERVICE_NAME = "workflow-bootstrap";

    /**
     * Feign 客户端扫描包。
     */
    public static final String CLIENT_BASE_PACKAGE = "io.gitee.dqcer.mcdull.workflow.facade.service";

    /**
     * 服务间契约统一前缀。
     */
    public static final String BASE_PATH = GlobalConstant.INNER_API + "/workflow/v1";

    /**
     * 批量读上限。
     */
    public static final int MAX_BATCH_READ_SIZE = 500;

    /**
     * 批量写上限。
     */
    public static final int MAX_BATCH_WRITE_SIZE = 100;

    /**
     * 游标分页上限。
     */
    public static final int MAX_PAGE_SIZE = 200;

    private WorkflowConstant() {
    }
}
