package com.dqcer.mcdull.uac.provider.web.manager.mdc;

import com.dqcer.mcdull.uac.api.vo.RemoteDictVO;

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
