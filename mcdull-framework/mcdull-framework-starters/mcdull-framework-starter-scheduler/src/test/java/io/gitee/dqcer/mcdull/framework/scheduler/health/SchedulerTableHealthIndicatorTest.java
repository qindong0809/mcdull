package io.gitee.dqcer.mcdull.framework.scheduler.health;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SchedulerTableHealthIndicatorTest {

    private final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    @Test
    void upWhenAllTablesPresent() {
        when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM scheduled_tasks", Integer.class)).thenReturn(0);
        when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM scheduler_task_execution", Integer.class)).thenReturn(0);

        Health health = new SchedulerTableHealthIndicator(jdbcTemplate, true).health();

        assertEquals(Status.UP, health.getStatus());
    }

    @Test
    void upWhenHistoryDisabledOnlyChecksMainTable() {
        when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM scheduled_tasks", Integer.class)).thenReturn(0);

        Health health = new SchedulerTableHealthIndicator(jdbcTemplate, false).health();

        assertEquals(Status.UP, health.getStatus());
        verify(jdbcTemplate, never()).queryForObject("SELECT COUNT(*) FROM scheduler_task_execution", Integer.class);
    }

    @Test
    void downWhenTableMissing() {
        when(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM scheduled_tasks", Integer.class))
                .thenThrow(new DataAccessResourceFailureException("table missing"));

        Health health = new SchedulerTableHealthIndicator(jdbcTemplate, true).health();

        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("table missing", health.getDetails().get("reason"));
    }
}
