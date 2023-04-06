package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.NoticeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.NoticeVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
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


    Result<List<Long>> logicDelete(List<Long> ids);

    /**
     * 分页查询
     *
     * @param dto 参数
     * @return {@link Result<PagedVO>}
     */
    Result<PagedVO<NoticeVO>> pagedQuery(NoticeLiteDTO dto);
}
