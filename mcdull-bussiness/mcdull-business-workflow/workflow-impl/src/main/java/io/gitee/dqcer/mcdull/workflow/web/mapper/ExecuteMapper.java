package io.gitee.dqcer.mcdull.workflow.web.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.workflow.model.dto.FlowTaskDTO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowHisTaskVO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowTaskVO;
import org.apache.ibatis.annotations.Param;

public interface ExecuteMapper {

    Page<FlowTaskVO> getTodoList(@Param("page") Page<?> page,
                                                @Param("task") FlowTaskDTO dto);

    Page<FlowHisTaskVO> copyPage(@Param("page") Page<?> page, @Param("dto") FlowTaskDTO dto);
}
