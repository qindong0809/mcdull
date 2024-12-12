package io.gitee.dqcer.mcdull.uac.provider.web.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Common service
 *
 * @author dqcer
 * @since 2024/7/25 9:19
 */

public interface ICommonManager {

    String convertDateTimeStr(Date date);

    String convertDateByUserTimezone(Date date);

    void exportExcel(String sheetName, String conditions, Map<String, String> titleMap, List<Map<String, String>> mapList);

    String readTemplateFileContent(String path);

    String replacePlaceholders(String template, Map<String, String> placeholders);
}
