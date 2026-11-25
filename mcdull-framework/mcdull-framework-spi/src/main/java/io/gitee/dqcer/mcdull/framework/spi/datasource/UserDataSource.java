package io.gitee.dqcer.mcdull.framework.spi.datasource;

/**
 * 用户绑定的数据源描述（跨 starter 的只读契约，不依赖任何 ORM / 连接池类型）。
 * <p>
 * 由用户数据源解析方（如 system 服务）提供：根据用户身份返回其所属数据库。
 * 框架层只消费这些字段构造物理数据源，不解释租户语义，也不回写。
 * <p>
 * 实现方通常是业务侧的远程传输 VO（如 RemoteUserDataSourceVO），
 * 本接口只暴露 getter，避免框架获得数据源配置的写权限。
 *
 * @author dqcer
 */
public interface UserDataSource {

    /** 数据源唯一标识，作为路由 lookupKey（如 tenant_42） */
    String getKey();

    /** 用户所属租户 ID，用于与请求头 tid 做一致性校验 */
    String getTenantId();

    String getUrl();

    String getUsername();

    String getPassword();
}
