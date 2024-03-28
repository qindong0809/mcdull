package io.gitee.dqcer.mcdull.mdc.provider.web.service;

import io.gitee.dqcer.mcdull.mdc.client.vo.DictTypeClientVO;

import java.util.List;

/**
 * @author dqcer
 * @since 2024/03/27
 */
public interface IDictTypeService {
    List<DictTypeClientVO> list(String selectType);
}
