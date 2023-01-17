package io.gitee.dqcer.mcdull.uac.provider.web.manager.mdc;

import io.gitee.dqcer.mcdull.uac.provider.model.vo.RemoteDictVO;

/**
 * 码表通用逻辑接口层
 *
 * @author dqcer
 * @version 2022/12/25
 */
public interface IDictManager {

    /**
     * 字典视图对象
     *
     * @param selectType 选择类型
     * @param code       代码
     * @return {@link RemoteDictVO}
     */
    RemoteDictVO dictVO(String selectType, String code);
}
