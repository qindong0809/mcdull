package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.NoticeVO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.NoticeLiteDTO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import java.util.List;

/**
* 通知公告表 业务接口类
*
* @author dqcer
* @since 2023-01-19
*/
public interface INoticeService {

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    Result<Long> insert(NoticeLiteDTO dto);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return {@link Result<NoticeVO> }
     */
    Result<NoticeVO> detail(Long id);

    /**
     * 编辑数据
     *
     * @param dto  参数
     * @return {@link Result<Long> }
     */
    Result<Long> update(NoticeLiteDTO dto);

    /**
     * 状态更新
     *
     * @param dto   参数
     * @return {@link Result<Long> }
     */
    Result<Long> updateStatus(StatusDTO dto);

    /**
     * 根据主键删除
     *
     * @param dto 参数
     * @return {@link Result<Long>}
     */
    Result<Long> delete(NoticeLiteDTO dto);

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
    Result<List<NoticeVO>> queryByIds(List<Long> ids);

    /**
     * 分页查询
     *
     * @param dto 参数
     * @return {@link Result<PagedVO>}
     */
    Result<PagedVO<NoticeVO>> listByPage(NoticeLiteDTO dto);
}
