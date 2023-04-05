package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.PostLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.PostVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

/**
 * 岗位服务 接口定义
 *
 * @author dqcer
 * @since 2023/01/15 15:01:98
 */
public interface IPostService {

    Result<PagedVO<PostVO>> pagedQuery(PostLiteDTO dto);

    Result<PostVO> detail(Long id);

    Result<Long> add(PostLiteDTO dto);

    Result<Long> update(PostLiteDTO dto);

    Result<Long> logicDelete(Long[] ids);
}
