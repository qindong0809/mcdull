package ${package.Service};

import com.dqcer.framework.base.vo.PagedVO;
import ${cfg.apiVo}.${cfg.voName};
import ${cfg.apiDto}.${cfg.dtoName};
import ${cfg.result};
import java.util.List;

/**
* ${table.comment!} 业务接口类
*
* @author ${author}
* @version ${date}
*/
public interface ${cfg.serviceName} {

    /**
     * 保存
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    Result<Long> save(${cfg.dtoName} dto);

    /**
     * 详情
     *
     * @param dto 参数
     * @return {@link Result<${cfg.voName}> }
     */
    Result<${cfg.voName}> detail(${cfg.dtoName} dto);

    /**
     * 更新
     *
     * @param param  参数
     * @return {@link Result<Long> }
     */
    Result<Long> update(${cfg.dtoName} param);

    /**
     * 更新状态
     *
     * @param dto    参数
     * @return {@link Result<Long> }
     */
    Result<Long> updateStatus(${cfg.dtoName} dto);

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
     * 分页列表
     *
     * @param dto 参数
     * @return {@link Result<PagedVO>}
     */
    Result<PagedVO<${cfg.voName}>> listByPage(${cfg.dtoName} dto);
}
