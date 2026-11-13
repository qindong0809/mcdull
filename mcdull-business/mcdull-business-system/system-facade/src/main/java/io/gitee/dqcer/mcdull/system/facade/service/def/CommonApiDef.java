package io.gitee.dqcer.mcdull.system.facade.service.def;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.facade.model.bo.DynamicFieldBO;
import io.gitee.dqcer.mcdull.system.facade.model.dto.SerialNumberGenerateDTO;
import io.gitee.dqcer.mcdull.system.facade.model.enums.DictSelectTypeEnum;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ApproveVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.IArea;
import io.gitee.dqcer.mcdull.system.facade.model.vo.IFileVO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 公共业务能力契约（仅本地直调）
 * <p>
 * 包含 Excel 导入导出、区域数据填充、文件 VO 回填、序列号生成、字典翻译等。
 * 这些能力依赖进程内的 VO 与回调函数，不适合跨服务远程调用，仅提供本地实现。
 */
public interface CommonApiDef {

    // ======================== 工具方法 ========================

    String convertDateTimeStr(Date date);

    String convertDateByUserTimezone(Date date);

    String getConfig(String key);

    Boolean getConfigToBool(String key);

    void downloadExcelTemplate(Map<String, List<DynamicFieldBO>> sheetMap, String fileNamePrefix);

    <D extends PagedDTO, V extends VO> void exportExcel(D dto, Function<D, PagedVO<V>> function,
                                                         String sheetName, List<Pair<String, Func1<V, ?>>> titlePairList);

    <V extends VO> void exportExcel(List<V> list, String sheetName, List<Pair<String, Func1<V, ?>>> titleFuncList);

    // ======================== 文件填充 ========================

    void setFileVO(List<? extends IFileVO> voList, Class<?> clazz);

    List<Pair<String, byte[]>> getFileDateList(Integer bizId, Class<?> clazz);

    // ======================== 审批人部门回填 ========================

    void setDepartment(List<? extends ApproveVO> voList, Integer userId);

    // ======================== 区域数据填充 ========================

    /**
     * 按 VO 列表批量填充省/市名称
     */
    void set(List<? extends IArea> voList);

    /**
     * 省份下拉选项
     */
    List<LabelValueVO<String, String>> provinceList();

    // ======================== 字典翻译 ========================

    /**
     * 翻译字典值
     */
    KeyValueBO<String, String> dictVO(DictSelectTypeEnum selectType, String value);

    // ======================== 序列号生成 ========================

    List<String> generate(SerialNumberGenerateDTO dto);

}
