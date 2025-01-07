package io.gitee.dqcer.mcdull.uac.provider.web.manager;

import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RemoteDictTypeVO;

import java.util.Map;

/**
 * 码表通用逻辑接口层
 *
 * @author dqcer
 * @since 2022/12/25
 */
public interface IDictTypeManager {

    /**
     * 字典视图对象
     *
     * @param selectTypeEnum 选择类型
     * @param code       代码
     * @return {@link RemoteDictTypeVO}
     */
    KeyValueBO<String, String> dictVO(IEnum<String> selectTypeEnum, String code);


    /**
     * code Name
     *
     * @param selectTypeEnum selectTypeEnum
     * @return {@link Map}<{@link String}, {@link String}>
     */
    Map<String, String> codeNameMap(IEnum<String> selectTypeEnum);

    void clean();

}
