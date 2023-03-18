package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.DictDataLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.DictDataVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

import java.util.List;

/**
 * 码表数据服务 接口定义
 *
 * @author dqcer
 * @since 2023/01/15 15:01:98
 */
public interface IDictDataService {

    Result<List<DictDataVO>> dictType(String dictType);

    Result<PagedVO<DictDataVO>> list(DictDataLiteDTO dto);
}
