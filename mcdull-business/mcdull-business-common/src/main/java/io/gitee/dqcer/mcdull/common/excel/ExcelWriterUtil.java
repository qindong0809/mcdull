package io.gitee.dqcer.mcdull.common.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel 写出能力（公共逻辑，不依赖任何数据库或服务）。
 * <p>
 * 由 system-impl 的 ExcelUtil 抽出写方法，供 blaze 与 system 两个服务共同引用。
 */
public class ExcelWriterUtil {

    public static void exportExcelByMap(OutputStream outputStream,
                                        String sheetName,
                                        String filterConditions,
                                        String exportBy,
                                        String exportTime,
                                        List<Pair<String, String>> list,
                                        List<Map<String, String>> dataMapList) {
        List<String> titleList = new ArrayList<>();
        for (Pair<String, String> pair : list) {
            titleList.add(pair.getKey());
        }
        exportExcel(outputStream, sheetName, filterConditions, exportBy, exportTime,
                toTitleList(titleList), toDataList(list, dataMapList));
    }

    private static void exportExcel(OutputStream outputStream,
                                    String sheetName,
                                    String filterConditions,
                                    String exportBy,
                                    String exportTime,
                                    List<List<String>> titleList,
                                    List<List<String>> dataList) {
        ExcelWriter writer = EasyExcel.write(outputStream).head(titleList).build();
        WriteSheet indexSheet = EasyExcel.writerSheet(GlobalConstant.Excel.INDEX)
                .registerWriteHandler(new IndexStyleCellWriteHandler())
                .needHead(false)
                .build();
        writer.write(indexDataList(filterConditions, exportBy, exportTime), indexSheet);
        WriteSheet dataSheet = EasyExcel.writerSheet(sheetName)
                .registerWriteHandler(defaultStyles())
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 25, (short) 25))
                .registerWriteHandler(new CustomSheetWriteHandler(titleList.size()))
                .build();
        writer.write(dataList, dataSheet);
        writer.finish();
    }

    public static HorizontalCellStyleStrategy defaultStyles() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteFont.setFontName("Calibri");
        headWriteFont.setFontHeightInPoints((short) 11);
        headWriteCellStyle.setWriteFont(headWriteFont);

        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("Calibri");
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteFont.setBold(false);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    public static List<List<Object>> indexDataList(String filterConditions, String exportBy, String exportTime) {
        List<List<Object>> list = ListUtils.newArrayList();
        List<Object> data = ListUtils.newArrayList();
        data.add("过滤条件: ");
        data.add(filterConditions);
        list.add(data);
        data = ListUtils.newArrayList();
        data.add("导出人: ");
        data.add(exportBy);
        list.add(data);
        data = ListUtils.newArrayList();
        data.add("导出时间: ");
        data.add(exportTime);
        list.add(data);
        return list;
    }

    private static List<List<String>> toTitleList(List<String> pairList) {
        if (CollUtil.isEmpty(pairList)) {
            return Collections.emptyList();
        }
        return pairList.stream().map(item -> ListUtil.toList(item.split(","))).collect(Collectors.toList());
    }

    private static List<List<String>> toDataList(List<Pair<String, String>> titleList, List<Map<String, String>> dataMapList) {
        if (CollUtil.isEmpty(dataMapList)) {
            return Collections.emptyList();
        }
        List<List<String>> dataList = new ArrayList<>();
        for (Map<String, String> map : dataMapList) {
            List<String> oneData = new ArrayList<>();
            for (Pair<String, String> pair : titleList) {
                oneData.add(map.get(pair.getValue()));
            }
            dataList.add(oneData);
        }
        return dataList;
    }
}
