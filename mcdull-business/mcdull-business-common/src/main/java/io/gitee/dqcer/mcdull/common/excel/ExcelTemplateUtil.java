package io.gitee.dqcer.mcdull.common.excel;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.support.ExcelField;
import io.gitee.dqcer.mcdull.framework.json.util.TimeZoneUtil;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 模板下载（公共逻辑，不依赖任何数据库或服务）。
 * <p>
 * 由 system 侧 CommonManagerImpl.downloadExcelTemplate 抽出，操作人名称与时间由调用方传入。
 */
public class ExcelTemplateUtil {

    private ExcelTemplateUtil() {
    }

    public static void write(OutputStream outputStream,
                             Map<String, ? extends List<? extends ExcelField>> sheetTemplateMap,
                             String operatorName,
                             String dateTimeStr) {
        if (MapUtil.isEmpty(sheetTemplateMap)) {
            return;
        }
        Map<String, List<List<String>>> sheetHeadMap = getSheetHeadMap(sheetTemplateMap);
        Map<String, Map<Integer, String[]>> sheetDropdownMap = getDropdownMap(sheetTemplateMap);
        List<Integer> dateIndexList = new ArrayList<>();
        Map<Integer, Integer[]> lengthMap = new HashMap<>(16);
        try (ExcelWriter excelWriter = EasyExcel.write(outputStream).build()) {
            WriteSheet indexSheet = EasyExcel.writerSheet(0, GlobalConstant.Excel.INDEX)
                    .registerWriteHandler(new IndexStyleCellWriteHandler())
                    .build();
            excelWriter.write(ExcelWriterUtil.indexDataList(StrUtil.EMPTY, operatorName, dateTimeStr), indexSheet);
            int index = 1;
            for (Map.Entry<String, List<List<String>>> entry : sheetHeadMap.entrySet()) {
                String sheetName = entry.getKey();
                WriteSheet writeSheet = EasyExcel.writerSheet(index++, sheetName)
                        .head(entry.getValue())
                        .registerWriteHandler(ExcelWriterUtil.defaultStyles())
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                        .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 25, (short) 25))
                        .registerWriteHandler(new CustomSheetWriteHandler(
                                sheetDropdownMap.get(sheetName), dateIndexList, lengthMap, sheetName, true, entry.getValue().size()))
                        .build();
                excelWriter.write(emptyDataList(sheetTemplateMap.get(sheetName), 30), writeSheet);
            }
        }
    }

    private static Map<String, List<List<String>>> getSheetHeadMap(Map<String, ? extends List<? extends ExcelField>> sheetTemplateMap) {
        Map<String, List<List<String>>> sheetHeadMap = new HashMap<>(8);
        for (Map.Entry<String, ? extends List<? extends ExcelField>> entry : sheetTemplateMap.entrySet()) {
            List<List<String>> headList = new ArrayList<>();
            for (ExcelField field : entry.getValue()) {
                headList.add(List.of(field.getName()));
            }
            sheetHeadMap.put(entry.getKey(), headList);
        }
        return sheetHeadMap;
    }

    private static Map<String, Map<Integer, String[]>> getDropdownMap(Map<String, ? extends List<? extends ExcelField>> mapField) {
        Map<String, Map<Integer, String[]>> sheetMap = new HashMap<>(8);
        for (Map.Entry<String, ? extends List<? extends ExcelField>> entry : mapField.entrySet()) {
            Map<Integer, String[]> map = new HashMap<>(16);
            List<? extends ExcelField> headers = entry.getValue();
            for (int i = 0; i < headers.size(); i++) {
                ExcelField field = headers.get(i);
                List<String> dropdownList = field.getDropdownList();
                if (field.isSelectable() && CollectionUtil.isNotEmpty(dropdownList)) {
                    map.put(i, ArrayUtil.toArray(dropdownList, String.class));
                }
            }
            sheetMap.put(entry.getKey(), map);
        }
        return sheetMap;
    }

    private static List<List<Object>> emptyDataList(List<? extends ExcelField> fieldList, int num) {
        List<List<Object>> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            List<Object> data = new ArrayList<>();
            for (int j = 0; j < fieldList.size(); j++) {
                data.add(StrUtil.EMPTY);
            }
            list.add(data);
        }
        return list;
    }

    public static String defaultDateTimeStr() {
        return TimeZoneUtil.serializeDate(new Date(), DatePattern.NORM_DATETIME_PATTERN, false);
    }
}
