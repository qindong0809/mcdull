package io.gitee.dqcer.mcdull.uac.provider.web.manager.mdc;

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
     * @param selectType 选择类型
     * @param code       代码
     * @return {@link RemoteDictTypeVO}
     */
    RemoteDictTypeVO dictVO(String selectType, String code);


    /**
     * code Name
     *
     * @param codeList   codeList
     * @param selectTypeEnum selectTypeEnum
     * @return {@link Map}<{@link String}, {@link String}>
     */
    Map<String, String> codeNameMap(IEnum<String> selectTypeEnum);
}
