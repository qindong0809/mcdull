package io.gitee.dqcer.mcdull.framework.scheduler.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SchedulerTableGuardTest {

    private final SchedulerAutoConfiguration configuration = new SchedulerAutoConfiguration();

    private DataSource dataSource;
    private Statement statement;
    private DatabaseMetaData metaData;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        statement = mock(Statement.class);
        metaData = mock(DatabaseMetaData.class);
        resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getTables(any(), any(), any(), any())).thenReturn(resultSet);
    }

    @Test
    void ddlSuccessReturnsNormally() throws Exception {
        when(statement.execute(anyString())).thenReturn(true);

        assertDoesNotThrow(() -> configuration.ensureTable(dataSource, "CREATE TABLE t (id INT)", "t"));
    }

    @Test
    void ddlFailureWithExistingTableReturnsNormally() throws Exception {
        doThrow(new SQLException("no create privilege")).when(statement).execute(anyString());
        when(resultSet.next()).thenReturn(true);

        assertDoesNotThrow(() -> configuration.ensureTable(dataSource, "CREATE TABLE t (id INT)", "t"));
    }

    @Test
    void ddlFailureWithMissingTableThrows() throws Exception {
        doThrow(new SQLException("no create privilege")).when(statement).execute(anyString());
        when(resultSet.next()).thenReturn(false);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> configuration.ensureTable(dataSource, "CREATE TABLE t (id INT)", "t"));
        assertTrue(ex.getMessage().contains("t"));
    }

    @Test
    void metaDataFailureThrows() throws Exception {
        doThrow(new SQLException("no create privilege")).when(statement).execute(anyString());
        when(metaData.getTables(any(), any(), any(), any())).thenThrow(new SQLException("meta failed"));

        assertThrows(IllegalStateException.class,
                () -> configuration.ensureTable(dataSource, "CREATE TABLE t (id INT)", "t"));
    }
}
