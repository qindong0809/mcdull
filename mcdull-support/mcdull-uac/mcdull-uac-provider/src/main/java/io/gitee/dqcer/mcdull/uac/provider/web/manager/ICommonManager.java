package io.gitee.dqcer.mcdull.uac.provider.web.manager;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.func.Func1;
import io.gitee.dqcer.mcdull.business.common.excel.DataAnalysisListener;
import io.gitee.dqcer.mcdull.business.common.excel.DynamicFieldTemplate;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.bo.DynamicFieldBO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Common service
 *
 * @author dqcer
 * @since 2024/7/25 9:19
 */

public interface ICommonManager {

    String convertDateTimeStr(Date date);

    String convertDateByUserTimezone(Date date);

    void downloadExcelTemplate(Map<String, List<DynamicFieldBO>> sheetMap, String fileNamePrefix);

    void importExcelData(MultipartFile file, Function<String, List<DynamicFieldTemplate>> function,
                         Consumer<List<Pair<String, DataAnalysisListener>>> consumer);

    <D extends PagedDTO, V extends VO> void exportExcel(D dto, Function<D, PagedVO<V>> function,
                                                           String sheetName, List<Pair<String, Func1<V, ?>>> titlePairList);

    void exportExcel(String sheetName, String conditions, List<Pair<String, String>> pairList, List<Map<String, String>> mapList);

    String readTemplateFileContent(String path);

    String replacePlaceholders(String template, Map<String, String> placeholders);

    String getConfig(String key);

    Boolean isCaptchaEnabled();

    Boolean getConfigToBool(String key);
}
