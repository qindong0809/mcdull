package io.gitee.dqcer.mcdull.framework.base.support;

import java.util.List;

/**
 * Excel 模板字段契约（轻量结构，不含任何业务语义）。
 * <p>
 * 放在 framework-base：common 的 Excel 工具与业务 facade 模型都依赖它，
 * 使 common 无需依赖任何业务 facade，同时 facade 也无需依赖重的 common 模块。
 *
 * @author dqcer
 */
public interface ExcelField {

    /** 表头名称 */
    String getName();

    /** 下拉选项（无下拉则返回 null 或空） */
    List<String> getDropdownList();

    /** 是否为选项类控件（由实现方用自身枚举判断） */
    boolean isSelectable();
}
