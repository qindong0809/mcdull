package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.MenuEntity;

import java.util.List;

/**
 * Role 管理器
 *
 * @author dqcer
 * @since 2024/10/31
 */
public interface IMenuManager {

    /**
     * 列出全部
     *
     * @return {@link List }<{@link MenuEntity }>
     */
    List<MenuEntity> listAll();

    /**
     * 获取名称代码列表
     *
     * @return {@link List }<{@link LabelValueVO }<{@link String }, {@link String }>>
     */
    List<LabelValueVO<String, String>> getNameCodeList();
}
