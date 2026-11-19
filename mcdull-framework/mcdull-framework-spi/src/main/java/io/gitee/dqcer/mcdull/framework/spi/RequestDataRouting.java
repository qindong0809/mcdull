package io.gitee.dqcer.mcdull.framework.spi;

/**
 * 请求级数据源路由。
 * <p>
 * 由数据源实现方（如 starter-mysql）提供，入口层（如 web 过滤器）只依赖本接口，
 * 因此在没有引入任何数据源组件的应用中，该能力缺席也不会阻断启动。
 *
 * @author dqcer
 * @since 2026/11/19
 */
public interface RequestDataRouting {

    /**
     * 未引入任何数据源组件时的空实现，使入口层不依赖数据源也能完成请求链路。
     */
    RequestDataRouting NOOP = new RequestDataRouting() {
        @Override
        public void switchDataSource() {
        }

        @Override
        public void removeDataSource() {
        }
    };

    /**
     * 按当前请求绑定数据源
     */
    void switchDataSource();

    /**
     * 解除当前线程的数据源绑定
     */
    void removeDataSource();
}
