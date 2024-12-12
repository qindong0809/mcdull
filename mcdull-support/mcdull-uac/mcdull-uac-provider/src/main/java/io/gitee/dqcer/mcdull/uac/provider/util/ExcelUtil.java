package io.gitee.dqcer.mcdull.uac.provider.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
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
import io.gitee.dqcer.mcdull.uac.provider.config.CustomSheetWriteHandler;
import io.gitee.dqcer.mcdull.uac.provider.config.IndexStyleCellWriteHandler;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel
 *
 * @author dqcer
 * @since 2024/06/18
 */
public class ExcelUtil {


    public static void main(String[] args) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        exportExcelByList(byteArrayOutputStream, "good job",
                StrUtil.EMPTY, StrUtil.EMPTY, StrUtil.EMPTY, ListUtil.of("name", "age"), dataList());
        FileUtil.writeBytes(byteArrayOutputStream.toByteArray(), "D:/22.xlsx");

        ByteArrayOutputStream byteArrayOutputStreamMap = new ByteArrayOutputStream();
        Map<String, String> map = new TreeMap<>();
        map.put("姓名", "name");
        map.put("年龄", "age");
        List<Map<String, String>> dataMapList = new ArrayList<>();
        Map<String, String> dataMap =  new TreeMap<>();
        dataMap.put("name", "张三");
        dataMap.put("age", "33");
        dataMapList.add(dataMap);
        exportExcelByMap(byteArrayOutputStreamMap, "map sheet",
                StrUtil.EMPTY, StrUtil.EMPTY, StrUtil.EMPTY, map, dataMapList);

        FileUtil.writeBytes(byteArrayOutputStream.toByteArray(), "D:/33.xlsx");

    }

    private static List<List<String>> dataList() {
        List<List<String>> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            List<String> data = ListUtils.newArrayList();
            data.add("name" + i);
            data.add("0.56");
            list.add(data);
        }
        return list;
    }

    public static void exportExcelByMap(OutputStream outputStream,
                                   String sheetName,
                                   String filterConditions,
                                   String exportBy,
                                   String exportTime,
                                   Map<String, String> titleMap,
                                   List<Map<String, String>> dataMapList) {
        exportExcel(outputStream, sheetName, filterConditions, exportBy, exportTime,
                toTitleList(titleMap), toDataList(titleMap, dataMapList));
    }


    public static void exportExcelByList(OutputStream outputStream,
                                   String sheetName,
                                   String filterConditions,
                                   String exportBy,
                                   String exportTime,
                                   List<String> titleList,
                                   List<List<String>> dataList) {
        exportExcel(outputStream, sheetName, filterConditions, exportBy, exportTime,
                dynamicHeader(titleList), dataList);
    }

    private static List<List<String>> dynamicHeader(List<String> titleList) {
        return titleList.stream()
                .map(item -> ListUtil.toList(item.split(",")))
                .collect(Collectors.toList());
    }

    private static void exportExcel(OutputStream outputStream,
                                   String sheetName,
                                   String filterConditions,
                                   String exportBy,
                                   String exportTime,
                                   List<List<String>> titleList,
                                   List<List<String>> dataList) {
        ExcelWriter writer = EasyExcel.write(outputStream).head(titleList)
                .build();
        WriteSheet indexSheet = EasyExcel.writerSheet("Index")
                .registerWriteHandler(new IndexStyleCellWriteHandler())
                .needHead(false)
                .build();
        writer.write(indexDataList(filterConditions, exportBy, exportTime), indexSheet);

        WriteSheet dataSheet = EasyExcel.writerSheet(sheetName)
                .registerWriteHandler(defaultStyles())
                // 自动计算列宽
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 25, (short) 25))
                .registerWriteHandler(new CustomSheetWriteHandler(titleList.size()))
                .build();
        writer.write(dataList,dataSheet);
        writer.finish();
    }

    public static HorizontalCellStyleStrategy defaultStyles() {
        // 表头样式策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteFont.setFontName("Calibri");
        headWriteFont.setFontHeightInPoints((short) 11);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容样式策略策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置背景颜色白色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        // 设置垂直居中为居中对齐
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置左右对齐为靠左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        // 设置单元格上下左右边框为细边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        //创建字体对象
        WriteFont contentWriteFont = new WriteFont();
        //内容字体大小
        contentWriteFont.setFontName("Calibri");
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteFont.setBold(false);//不加粗
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 初始化表格样式
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }


    private static List<List<Object>> indexDataList(String filterConditions, String exportBy, String exportTime) {
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



    private static List<List<String>> toTitleList(Map<String, String> titleMap) {
        if (MapUtil.isEmpty(titleMap)) {
            return Collections.emptyList();
        }
        List<List<String>> titleList = new ArrayList<>();
        for (Map.Entry<String, String> entry : titleMap.entrySet()) {
            List<String> oneTitle = new ArrayList<>();
            oneTitle.add(entry.getKey());
            titleList.add(oneTitle);
        }
        return titleList;
    }

    private static List<List<String>> toDataList(Map<String, String> titleMap, List<Map<String, String>> dataMapList) {
        if (CollUtil.isEmpty(dataMapList)) {
            return Collections.emptyList();
        }
        List<List<String>> dataList = new ArrayList<>();
        for (Map<String, String> map : dataMapList) {
            List<String> oneData = new ArrayList<>();
            for (Map.Entry<String, String> entry : titleMap.entrySet()) {
                oneData.add(map.get(entry.getValue()));
            }
            dataList.add(oneData);
        }
        return dataList;
    }
}
