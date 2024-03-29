package io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.LogEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.LogVO;
import org.apache.ibatis.annotations.Param;

/**
 * 日志记录 Mapper 接口
 *
 * @author dqcer
 * @since 2023-01-14
 */
public interface LogMapper extends BaseMapper<LogEntity> {

    IPage<LogVO> pagedQuery(Page<LogEntity> objectPage, @Param("param") LogLiteDTO param);
}