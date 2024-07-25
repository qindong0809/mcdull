package io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.TableQueryForm;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.CodeGeneratorConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableColumnVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Code generator config mapper
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface CodeGeneratorConfigMapper extends BaseMapper<CodeGeneratorConfigEntity> {

    List<TableColumnVO> getByTable(@Param("tableName") String table);

    List<TableVO> queryTableList(@Param("page") Page<?> page, @Param("queryForm") TableQueryForm queryForm);

    long existByTable(@Param("tableName") String tableName);
}