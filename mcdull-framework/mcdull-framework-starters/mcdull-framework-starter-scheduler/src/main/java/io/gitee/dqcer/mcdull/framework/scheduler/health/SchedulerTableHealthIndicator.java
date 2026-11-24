package io.gitee.dqcer.mcdull.framework.scheduler.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 调度表健康检查。
 * <p>
 * 启动时的 fail-fast 只能保证建表成功，运行中表被误删仍需通过 {@code /actuator/health} 暴露。
 *
 * @author dqcer
 * @since 1.0.0
 */
public class SchedulerTableHealthIndicator implements HealthIndicator {

    private static final String MAIN_TABLE = "scheduled_tasks";
    private static final String HISTORY_TABLE = "scheduler_task_execution";

    private final JdbcTemplate jdbcTemplate;
    private final boolean historyEnabled;

    public SchedulerTableHealthIndicator(JdbcTemplate jdbcTemplate, boolean historyEnabled) {
        this.jdbcTemplate = jdbcTemplate;
        this.historyEnabled = historyEnabled;
    }

    @Override
    public Health health() {
        try {
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + MAIN_TABLE, Integer.class);
            if (historyEnabled) {
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + HISTORY_TABLE, Integer.class);
            }
            String[] tables = historyEnabled
                    ? new String[]{MAIN_TABLE, HISTORY_TABLE}
                    : new String[]{MAIN_TABLE};
            return Health.up().withDetail("tables", tables).build();
        } catch (Exception e) {
            String reason = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            return Health.down(e).withDetail("reason", reason).build();
        }
    }
}
