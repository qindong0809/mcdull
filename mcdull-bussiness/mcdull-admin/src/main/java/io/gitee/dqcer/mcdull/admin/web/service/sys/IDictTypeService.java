package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictTypeLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictTypeVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

import java.util.List;

/**
 * 码表类型服务 接口定义
 *
 * @author dqcer
 * @since 2023/01/15 15:01:98
 */
public interface IDictTypeService {

    Result<PagedVO<DictTypeVO>> list(DictTypeLiteDTO dto);

    Result<DictTypeVO> detail(Long dictId);

    Result<List<DictTypeVO>> getAll();

    Result<Long> add(DictTypeAddDTO dto);

    Result<Long> edit(DictTypeEditDTO dto);

    Result<Long> remove(Long id);
}
