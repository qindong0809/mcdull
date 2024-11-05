package io.gitee.dqcer.mcdull.uac.provider.web.manager;

import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileExtensionTypeEnum;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Common service
 *
 * @author dqcer
 * @since 2024/7/25 9:19
 */

public interface ICommonManager {

    String getFileName(FileExtensionTypeEnum fileExtension, String... args);

    void storageExcel(String modelName, String suffixFileName, String condition,
                 Supplier<Map<String, String>> supplierTitleMap,
                 Supplier<List<Map<String, String>>> supplierDataList);

    String convertDateTimeStr(Date date);

    void exportExcel(String sheetName, String conditions, Map<String, String> titleMap, List<Map<String, String>> mapList);
}
