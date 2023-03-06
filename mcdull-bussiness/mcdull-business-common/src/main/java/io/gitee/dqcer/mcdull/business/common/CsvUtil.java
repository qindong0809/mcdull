package io.gitee.dqcer.mcdull.business.common;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.csv.CsvReader;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * csv操作工具类
 *
 * @author dqcer
 * @since 2023/03/06
 */
public class CsvUtil {


    /**
     * 解析csv文件
     *
     * @param inputStream   输入流
     * @param searchMap     模糊查询
     * @param displayFields 指定显示的字段
     * @return {@link List}<{@link List}<{@link String}>>
     */
    public static List<List<String>> parse(InputStream inputStream, Map<String, String> searchMap, List<String> displayFields) {
        // 读取utf-8编码的csv文件，需要使用BOMInputStream去掉BOM头
        InputStreamReader inputStreamReader = new InputStreamReader(new BOMInputStream(inputStream), StandardCharsets.UTF_8);
        CsvReader reader = cn.hutool.core.text.csv.CsvUtil.getReader();
        List<List<String>> data = new ArrayList<>();
        List<Map<String, String>> mapList = reader.readMapList(inputStreamReader);
        for (Map<String, String> map : mapList) {
            boolean isOk = match(searchMap, map);
            if (isOk) {
                List<String> fieldValues = filterField(displayFields, map);
                data.add(fieldValues);
            }
        }
        return data;
    }


    /**
     * 关键字模糊查询
     *
     * @param searchMap     模糊查询
     * @param currentRowMap 当前行
     * @return boolean
     */
    private static boolean match(Map<String, String> searchMap, Map<String, String> currentRowMap) {
        if (MapUtil.isEmpty(searchMap)) {
            return true;
        }
        for (Map.Entry<String, String> entry : searchMap.entrySet()) {
            String key = entry.getKey();
            String sourceValue = currentRowMap.get(key);
            if (sourceValue != null) {
                if (!sourceValue.contains(entry.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 过滤不需要显示的字段对应的值
     *
     * @param displayFields 显示字段
     * @param currentRowMap 当前行
     * @return {@link List}<{@link String}>
     */
    private static List<String> filterField(List<String> displayFields, Map<String, String> currentRowMap) {
        List<String> rowData = new ArrayList<>();
        if (displayFields == null) {
            return rowData;
        }
        for (String displayField : displayFields) {
            boolean containsKey = currentRowMap.containsKey(displayField);
            if (!containsKey) {
                throw new IllegalArgumentException(String.format("Argument Exception. displayField: '%s'", displayField));
            }
            rowData.add(currentRowMap.get(displayField));
        }
        return rowData;
    }

    public static void responseSetProperties(String fileName, byte[] bytes, HttpServletResponse response) throws IOException {
        fileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        response.setCharacterEncoding("UTF-8");
        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/csv");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "fileName");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        OutputStream outputStream = response.getOutputStream();
        // 加入BOM头防止Excel读取utf-8编码文件乱码
        outputStream.write(0xEF);
        outputStream.write(0xBB);
        outputStream.write(0xBF);
        outputStream.write(bytes);
        outputStream.flush();
    }
}
