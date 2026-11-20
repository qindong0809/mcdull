package io.gitee.dqcer.mcdull.demo.facade.constant;

/**
 * demo 模块对外常量。
 */
public final class DemoConstant {

    /**
     * 服务名，与 bootstrap 的 spring.application.name 保持一致。
     */
    public static final String SERVICE_NAME = "demo-bootstrap";

    /**
     * 任务接口统一前缀。
     */
    public static final String TASK_PATH = "/demo/task";

    private DemoConstant() {
    }
}
