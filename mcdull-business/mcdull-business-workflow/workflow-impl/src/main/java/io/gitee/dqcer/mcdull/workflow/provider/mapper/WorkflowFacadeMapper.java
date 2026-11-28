package io.gitee.dqcer.mcdull.workflow.web.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * workflow facade 专用 Mapper。
 * <p>
 * 只查询 flow_* 表，不允许关联其他服务拥有权的业务表。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public interface WorkflowFacadeMapper {

    int insertEvent(@Param("row") Map<String, Object> row);

    int insertHistory(@Param("row") Map<String, Object> row);
}
