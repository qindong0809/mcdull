package ${package.Service};

import ${cfg.PagedVO};
import ${cfg.StatusDTO};
import ${cfg.apiVo}.${cfg.voName};
import ${cfg.apiDto}.${cfg.dtoName};
import ${cfg.result};
import java.util.List;

/**
* ${table.comment!} 业务接口类
*
* @author ${author}
* @since ${date}
*/
public interface ${cfg.serviceName} {

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    Result<Long> insert(${cfg.dtoName} dto);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return {@link Result<${cfg.voName}> }
     */
    Result<${cfg.voName}> detail(Long id);

    /**
     * 编辑数据
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    Result<Long> update(${cfg.dtoName} dto);
<#list table.fields as field>
    <#if "status" == field.name>
    /**
     * 状态更新
     *
     * @param dto   参数
     * @return {@link Result<Long> }
     */
     Result<Long> updateStatus(StatusDTO dto);
    </#if>
</#list>

    /**
     * 根据主键删除
     *
     * @param dto 参数
     * @return {@link Result<Long>}
     */
    Result<Long> delete(${cfg.dtoName} dto);

    /**
     * 根据主键集删除
     *
     * @param ids id集
     * @return {@link Result<List>}
     */
    Result<List<Long>> deleteByIds(List<Long> ids);

    /**
     * 根据主键集查询
     *
     * @param ids id
     * @return {@link Result<List>}
     */
    Result<List<${cfg.voName}>> queryByIds(List<Long> ids);

    /**
     * 分页查询
     *
     * @param dto 参数
     * @return {@link Result<PagedVO>}
     */
    Result<PagedVO<${cfg.voName}>> listByPage(${cfg.dtoName} dto);
}
